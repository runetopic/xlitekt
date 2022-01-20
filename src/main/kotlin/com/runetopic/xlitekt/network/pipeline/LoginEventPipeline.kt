package com.runetopic.xlitekt.network.pipeline

import com.runetopic.cache.store.Js5Store
import com.runetopic.cryptography.fromXTEA
import com.runetopic.xlitekt.network.NetworkOpcode.LOGIN_NORMAL_OPCODE
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.BAD_SESSION_OPCODE
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.CLIENT_OUTDATED_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.application.ApplicationEnvironment
import org.slf4j.Logger
import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 */
class LoginEventPipeline : EventPipeline<ReadEvent.LoginReadEvent, WriteEvent.LoginWriteEvent> {

    private val logger by inject<Logger>()
    private val store by inject<Js5Store>()
    private val environment by inject<ApplicationEnvironment>()

    override suspend fun read(client: Client): ReadEvent.LoginReadEvent? {
        val opcode = client.readChannel.readByte().toInt() and 0xff

        if (!isValidLoginRequestHeader(client)) {
            logger.info("Invalid login request header. Client=$client Opcode=$opcode Available bytes=${client.readChannel.availableForRead}")
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
                val rsaBlock = ByteBuffer.wrap(
                    BigInteger(rsa).modPow(
                        BigInteger(environment.config.property("game.rsa.exponent").getString()),
                        BigInteger(environment.config.property("game.rsa.modulus").getString())
                    ).toByteArray()
                )
                if (!isValidLoginRSA(rsaBlock)) {
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }
                val clientKeys = IntArray(4) { rsaBlock.int }
                val clientSeed = rsaBlock.long

                if (clientSeed != client.seed) {
                    client.writeResponse(BAD_SESSION_OPCODE)
                    logger.info("Bad Session. Client/Server seed miss-match. ClientSeed=$clientSeed Seed=${client.seed}")
                    return null
                }

                if (isValidAuthenticationType(rsaBlock)) {
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }

                rsaBlock.get() // Unknown byte #3
                val password = rsaBlock.readString()
                val xteaBytes = ByteArray(client.readChannel.availableForRead)
                client.readChannel.readAvailable(xteaBytes, 0, xteaBytes.size)
                val xteaBlock = ByteBuffer.wrap(xteaBytes.fromXTEA(32, clientKeys))
                val username = xteaBlock.readString()

                val clientSettings = xteaBlock.get().toInt()
                val clientResizeable = (clientSettings shr 1) == 1
                val clientWidth = xteaBlock.short.toInt() and 0xffff
                val clientHeight = xteaBlock.short.toInt() and 0xffff
                xteaBlock.position(xteaBlock.position() + 24)
                val token = xteaBlock.readString() // Token in jav_config

                logger.info("Token $token")
                xteaBlock.int // Unknown Int #1

                readMachineInformation(xteaBlock)

                val clientType = xteaBlock.get().toInt() and 0xff

                // TODO make the cache library expose # of indexes available
                val cacheCRCs = IntArray(20) { store.index(it).crc }
                val clientCRCs = IntArray(20) { -1 }

                if (xteaBlock.int != 0 && xteaBlock.int != 0) {
                    client.writeResponse(BAD_SESSION_OPCODE)
                    return null
                }

//                clientCRCs[6] = xteaBlock.readIntLE()
//                clientCRCs[1] = xteaBlock.readIntV2()
//                clientCRCs[14] = xteaBlock.readIntLE()
//                clientCRCs[13] = xteaBlock.readIntV1()
//                clientCRCs[12] = xteaBlock.readInt()
//                clientCRCs[19] = xteaBlock.readInt()
//                clientCRCs[15] = xteaBlock.readIntLE()
//                clientCRCs[3] = xteaBlock.readIntV2()
//                clientCRCs[8] = xteaBlock.readIntV2()
//                clientCRCs[17] = xteaBlock.readIntV1()
//                clientCRCs[7] = xteaBlock.readIntV2()
//                clientCRCs[11] = xteaBlock.readInt()
//                clientCRCs[18] = xteaBlock.readIntLE()
//                clientCRCs[5] = xteaBlock.readInt()
//                clientCRCs[2] = xteaBlock.readIntV2()
//                clientCRCs[4] = xteaBlock.readIntLE()
//                clientCRCs[9] = xteaBlock.readIntV2()
//                clientCRCs[10] = xteaBlock.readInt()
//                clientCRCs[20] = xteaBlock.readIntLE()
//                clientCRCs[0] = xteaBlock.readIntLE()

                logger.info("Username $username Password $password Client Type $clientType")
            }
        }
        return ReadEvent.LoginReadEvent(opcode)
    }

    private fun readMachineInformation(xteaBlock: ByteBuffer) {
        xteaBlock.get()
        xteaBlock.get()
        xteaBlock.get()
        xteaBlock.short
        xteaBlock.get()
        xteaBlock.get()
        xteaBlock.get()
        xteaBlock.get()
        xteaBlock.get()
        xteaBlock.short
        xteaBlock.get()
        xteaBlock.position(xteaBlock.position() + 3)
        xteaBlock.short
        xteaBlock.readTerminatedString()
        xteaBlock.readTerminatedString()
        xteaBlock.readTerminatedString()
        xteaBlock.readTerminatedString()
        xteaBlock.get()
        xteaBlock.short
        xteaBlock.readTerminatedString()
        xteaBlock.readTerminatedString()
        xteaBlock.get()
        xteaBlock.get()
        xteaBlock.int
        xteaBlock.int
        xteaBlock.int
        xteaBlock.int
        xteaBlock.readTerminatedString()
    }

    private fun isValidAuthenticationType(rsaBlock: ByteBuffer): Boolean {
        when (val authenticationType = rsaBlock.get().toInt()) {
            1 -> rsaBlock.position(rsaBlock.position() + 4)
            0, 3 -> rsaBlock.position(rsaBlock.position() + 3)
            2 -> rsaBlock.position(rsaBlock.position() + 4)
            else -> {
                logger.info("Bad Session. Unhandled authentication type=$authenticationType")
                return false
            }
        }
        return true
    }

    private fun isValidLoginRSA(buffer: ByteBuffer) = buffer.get().toInt() and 0xff == 1

    private suspend fun isValidLoginRequestHeader(client: Client): Boolean {
        val size = client.readChannel.readShort().toInt() and 0xffff
        val availableBytes = client.readChannel.availableForRead

        if (size != availableBytes) {
            logger.info("Bad session. Client=$client Size Read=$size Available bytes=$availableBytes")
            client.writeResponse(BAD_SESSION_OPCODE)
            return false
        }

        val majorVersion = client.readChannel.readInt()

        if (majorVersion != environment.config.property("game.build.major").getString().toInt()) {
            logger.info("Client outdated. Client=$client Major Version=$majorVersion")
            client.writeResponse(CLIENT_OUTDATED_OPCODE)
            return false
        }

        val minorVersion = client.readChannel.readInt()

        if (minorVersion != environment.config.property("game.build.minor").getString().toInt()) {
            logger.info("Client outdated. Client=$client Minor Version=$majorVersion")
            client.writeResponse(CLIENT_OUTDATED_OPCODE)
            return false
        }

        return true
    }

    override suspend fun write(client: Client, event: WriteEvent.LoginWriteEvent) {
    }

    // TODO move this out most likely into it's own file
    private fun ByteBuffer.readString(): String {
        var s = ""
        var b: Int
        while (get().toInt().also { b = it } != 0) s += b.toChar()
        return s
    }

    private fun ByteBuffer.readTerminatedString(): String {
        if (get().toInt() != 0) throw IllegalArgumentException()
        return readString()
    }
}
