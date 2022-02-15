package com.runetopic.xlitekt.network.pipeline

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.fromXTEA
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.LOGIN_NORMAL_OPCODE
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.BAD_SESSION_OPCODE
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.CLIENT_OUTDATED_OPCODE
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.LOGIN_SUCCESS_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.GameEventHandler
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.ext.readIntV1
import com.runetopic.xlitekt.util.ext.readIntV2
import com.runetopic.xlitekt.util.ext.readMedium
import com.runetopic.xlitekt.util.ext.readStringCp1252NullCircumfixed
import com.runetopic.xlitekt.util.ext.readStringCp1252NullTerminated
import com.runetopic.xlitekt.util.ext.toBoolean
import io.ktor.application.ApplicationEnvironment
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readIntLittleEndian
import io.ktor.utils.io.core.readLong
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kotlinx.coroutines.withTimeout
import java.math.BigInteger

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 */
class LoginEventPipeline : EventPipeline<ReadEvent.LoginReadEvent, WriteEvent.LoginWriteEvent> {

    private val logger = InlineLogger()
    private val store by inject<Js5Store>()
    private val environment by inject<ApplicationEnvironment>()
    private val token = environment.config.property("game.build.token").getString()
    private val timeout = environment.config.property("network.timeout").getString().toLong()

    override suspend fun read(client: Client): ReadEvent.LoginReadEvent? {
        if (client.readChannel.availableForRead <= 0) {
            withTimeout(timeout) { client.readChannel.awaitContent() }
        }

        val opcode = client.readChannel.readByte().toInt() and 0xff

        if (!isValidLoginRequestHeader(client)) {
            logger.debug { "Invalid login request header. Client=$client Opcode=$opcode Available bytes=${client.readChannel.availableForRead}" }
            return null
        }

        client.readChannel.readByte() // Unknown byte #1
        client.readChannel.readByte() // Unknown byte #2

        when (opcode) {
            LOGIN_NORMAL_OPCODE -> {
                val rsa = ByteArray(client.readChannel.readShort().toInt() and 0xffff)
                if (client.readChannel.readAvailable(rsa, 0, rsa.size) != rsa.size) {
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }

                val rsaBlock = ByteReadPacket(
                    BigInteger(rsa).modPow(
                        BigInteger(environment.config.property("game.rsa.exponent").getString()),
                        BigInteger(environment.config.property("game.rsa.modulus").getString())
                    ).toByteArray()
                )

                if (!isValidLoginRSA(rsaBlock)) {
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }
                val clientKeys = IntArray(4) { rsaBlock.readInt() }
                val clientSeed = rsaBlock.readLong()

                if (clientSeed != client.seed) {
                    logger.debug { "Bad Session. Client/Server seed miss-match. ClientSeed=$clientSeed Seed=${client.seed}" }
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }

                if (!isValidAuthenticationType(rsaBlock)) {
                    logger.debug { "Bad Session. Authentication type is not valid." }
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }

                rsaBlock.readByte() // Unknown byte #3
                val password = rsaBlock.readStringCp1252NullTerminated()
                rsaBlock.release()
                val xtea = ByteArray(client.readChannel.availableForRead)
                client.readChannel.readAvailable(xtea, 0, xtea.size)
                val xteaBlock = ByteReadPacket(xtea.fromXTEA(32, clientKeys))
                val username = xteaBlock.readStringCp1252NullTerminated()
                val clientSettings = xteaBlock.readByte().toInt()
                val clientResizeable = (clientSettings shr 1) == 1
                val clientWidth = xteaBlock.readUShort().toInt()
                val clientHeight = xteaBlock.readUShort().toInt()
                xteaBlock.discard(24)

                val token = xteaBlock.readStringCp1252NullTerminated()
                if (!isValidToken(token)) {
                    logger.debug { "Bad Session. Gamepack token is not valid. Token was $token." }
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }

                xteaBlock.readInt() // Unknown Int #1
                readMachineInformation(xteaBlock)
                val clientType = xteaBlock.readUByte().toInt()
                val cacheCRCs = IntArray(21) { store.index(it).crc } // TODO make the cache library expose # of indexes available
                val clientCRCs = IntArray(21) { -1 }

                if (xteaBlock.readInt() != 0 || xteaBlock.readInt() != 0) {
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }

                clientCRCs[6] = xteaBlock.readIntLittleEndian()
                clientCRCs[1] = xteaBlock.readIntV2()
                clientCRCs[14] = xteaBlock.readIntLittleEndian()
                clientCRCs[13] = xteaBlock.readIntV1()
                clientCRCs[12] = xteaBlock.readInt()
                clientCRCs[19] = xteaBlock.readInt()
                clientCRCs[15] = xteaBlock.readIntLittleEndian()
                clientCRCs[3] = xteaBlock.readIntV2()
                clientCRCs[8] = xteaBlock.readIntV2()
                clientCRCs[17] = xteaBlock.readIntV1()
                clientCRCs[7] = xteaBlock.readIntV2()
                clientCRCs[11] = xteaBlock.readInt()
                clientCRCs[18] = xteaBlock.readIntLittleEndian()
                clientCRCs[5] = xteaBlock.readInt()
                clientCRCs[2] = xteaBlock.readIntV2()
                clientCRCs[4] = xteaBlock.readIntLittleEndian()
                clientCRCs[9] = xteaBlock.readIntV2()
                clientCRCs[10] = xteaBlock.readInt()
                clientCRCs[20] = xteaBlock.readIntLittleEndian()
                clientCRCs[0] = xteaBlock.readIntLittleEndian()
                clientCRCs[16] = cacheCRCs[16] // This is -1 from the client.

                xteaBlock.release()

                if (!isValidCacheCRCs(clientCRCs)) {
                    logger.debug { "Bad Session. Client and cache crc are mismatched." }
                    client.writeResponse(CLIENT_OUTDATED_OPCODE)
                    return null
                }

                val serverKeys = IntArray(clientKeys.size) { clientKeys[it] + 50 }

                return ReadEvent.LoginReadEvent(
                    opcode,
                    clientKeys.toList(),
                    serverKeys.toList(),
                    username,
                    password,
                    clientResizeable,
                    clientWidth,
                    clientHeight,
                    clientType
                )
            }
        }
        return null
    }

    override suspend fun write(client: Client, event: WriteEvent.LoginWriteEvent) {
        client.writeResponse(event.response)

        if (event.response == LOGIN_SUCCESS_OPCODE) {
            val player = client.player ?: return client.disconnect("Login write event does not have an established player.")

            client.writeChannel.apply {
                writeByte(11)
                writeByte(0)
                writeInt(0)
                writeByte(player.rights.toByte())
                writeByte(0)
                writeShort(player.index.toShort())
                writeByte(0)
            }.flush()

            client.useEventPipeline(inject<GameEventPipeline>())
            client.useEventHandler(inject<GameEventHandler>())

            player.login(client)
        }
    }

    private fun readMachineInformation(xteaBlock: ByteReadPacket) {
        xteaBlock.readByte()
        xteaBlock.readByte()
        xteaBlock.readByte()
        xteaBlock.readShort()
        xteaBlock.readByte()
        xteaBlock.readByte()
        xteaBlock.readByte()
        xteaBlock.readByte()
        xteaBlock.readByte()
        xteaBlock.readShort()
        xteaBlock.readByte()
        xteaBlock.readMedium()
        xteaBlock.readShort()
        xteaBlock.readStringCp1252NullCircumfixed()
        xteaBlock.readStringCp1252NullCircumfixed()
        xteaBlock.readStringCp1252NullCircumfixed()
        xteaBlock.readStringCp1252NullCircumfixed()
        xteaBlock.readByte()
        xteaBlock.readShort()
        xteaBlock.readStringCp1252NullCircumfixed()
        xteaBlock.readStringCp1252NullCircumfixed()
        xteaBlock.readByte()
        xteaBlock.readByte()
        xteaBlock.readInt()
        xteaBlock.readInt()
        xteaBlock.readInt()
        xteaBlock.readInt()
        xteaBlock.readStringCp1252NullCircumfixed()
    }

    private suspend fun isValidLoginRequestHeader(client: Client): Boolean {
        val size = client.readChannel.readShort().toInt() and 0xffff
        val availableBytes = client.readChannel.availableForRead

        if (size != availableBytes) {
            logger.debug { "Bad session. Client=$client Size Read=$size Available bytes=$availableBytes" }
            client.writeResponse(BAD_SESSION_OPCODE)
            return false
        }

        val majorVersion = client.readChannel.readInt()

        if (majorVersion != environment.config.property("game.build.major").getString().toInt()) {
            logger.debug { "Client outdated. Client=$client Major Version=$majorVersion" }
            client.writeResponse(CLIENT_OUTDATED_OPCODE)
            return false
        }

        val minorVersion = client.readChannel.readInt()

        if (minorVersion != environment.config.property("game.build.minor").getString().toInt()) {
            logger.debug { "Client outdated. Client=$client Minor Version=$majorVersion" }
            client.writeResponse(CLIENT_OUTDATED_OPCODE)
            return false
        }

        return true
    }

    private fun isValidLoginRSA(buffer: ByteReadPacket) = buffer.readByte().toInt().toBoolean()

    private fun isValidAuthenticationType(rsaBlock: ByteReadPacket): Boolean {
        when (val authenticationType = rsaBlock.readByte().toInt()) {
            1 -> rsaBlock.discard(4)
            0, 3 -> rsaBlock.discard(3)
            2 -> rsaBlock.discard(4)
            else -> {
                logger.debug { "Bad Session. Unhandled authentication type=$authenticationType" }
                return false
            }
        }
        return true
    }

    private fun isValidToken(token: String): Boolean = this.token == token

    private fun isValidCacheCRCs(clientCRCs: IntArray): Boolean = IntArray(21) { store.index(it).crc }.contentEquals(clientCRCs)
}
