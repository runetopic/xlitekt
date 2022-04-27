package xlitekt.network.client

import com.runetopic.cryptography.fromXTEA
import com.runetopic.cryptography.toISAAC
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readIntLittleEndian
import io.ktor.utils.io.core.readLong
import io.ktor.utils.io.core.readShort
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kotlinx.coroutines.withTimeout
import xlitekt.game.actor.player.Client
import xlitekt.game.actor.player.Client.Companion.checksums
import xlitekt.game.actor.player.Client.Companion.majorBuild
import xlitekt.game.actor.player.Client.Companion.minorBuild
import xlitekt.game.actor.player.Client.Companion.rsaExponent
import xlitekt.game.actor.player.Client.Companion.rsaModulus
import xlitekt.game.actor.player.Client.Companion.sizes
import xlitekt.game.actor.player.Client.Companion.store
import xlitekt.game.actor.player.Client.Companion.world
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.PlayerDecoder
import xlitekt.game.content.ui.InterfaceLayout
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.game.packet.disassembler.handler.PacketHandler
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.network.client.ClientRequestOpcode.HANDSHAKE_JS5_OPCODE
import xlitekt.network.client.ClientRequestOpcode.HANDSHAKE_LOGIN_OPCODE
import xlitekt.network.client.ClientRequestOpcode.JS5_ENCRYPTION_OPCODE
import xlitekt.network.client.ClientRequestOpcode.JS5_HIGH_PRIORITY_OPCODE
import xlitekt.network.client.ClientRequestOpcode.JS5_LOGGED_IN_OPCODE
import xlitekt.network.client.ClientRequestOpcode.JS5_LOW_PRIORITY_OPCODE
import xlitekt.network.client.ClientRequestOpcode.JS5_SWITCH_OPCODE
import xlitekt.network.client.ClientRequestOpcode.LOGIN_NORMAL_OPCODE
import xlitekt.network.client.ClientResponseOpcode.BAD_SESSION_OPCODE
import xlitekt.network.client.ClientResponseOpcode.CLIENT_OUTDATED_OPCODE
import xlitekt.network.client.ClientResponseOpcode.HANDSHAKE_SUCCESS_OPCODE
import xlitekt.network.client.ClientResponseOpcode.LOGIN_SUCCESS_OPCODE
import xlitekt.shared.buffer.readIntV1
import xlitekt.shared.buffer.readIntV2
import xlitekt.shared.buffer.readPacketOpcode
import xlitekt.shared.buffer.readPacketSize
import xlitekt.shared.buffer.readStringCp1252NullCircumfixed
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUMedium
import xlitekt.shared.toBoolean
import java.math.BigInteger
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
private suspend fun Client.writeResponse(response: Int) = writeChannel!!.apply { writeByte(response.toByte()) }.flush()

suspend fun Client.readHandshake() {
    val readChannel = readChannel ?: return
    when (val opcode = readChannel.readByte().toInt()) {
        HANDSHAKE_JS5_OPCODE -> writeHandshake(opcode, readChannel.readInt())
        HANDSHAKE_LOGIN_OPCODE -> writeHandshake(opcode, -1)
        else -> throw IllegalStateException("Unhandled opcode found during client/server handshake. Opcode=$opcode")
    }
}

private suspend fun Client.writeHandshake(opcode: Int, version: Int) {
    val response = when (opcode) {
        HANDSHAKE_JS5_OPCODE -> if (version == majorBuild) HANDSHAKE_SUCCESS_OPCODE else CLIENT_OUTDATED_OPCODE
        HANDSHAKE_LOGIN_OPCODE -> HANDSHAKE_SUCCESS_OPCODE
        else -> throw IllegalStateException("Unhandled opcode in handshake handler. Opcode=$opcode")
    }
    writeResponse(response)
    if (response != HANDSHAKE_SUCCESS_OPCODE) {
        return disconnect("Handshake response was not successful. Response was $response.")
    }
    when (opcode) {
        HANDSHAKE_JS5_OPCODE -> readJS5File()
        HANDSHAKE_LOGIN_OPCODE -> {
            writeChannel!!.apply {
                writeLong(seed)
            }.flush()
            readLogin()
        }
    }
}

private suspend fun Client.readJS5File() = try {
    while (true) {
        withTimeout(30_000) {
            val readChannel = readChannel ?: return@withTimeout
            when (val opcode = readChannel.readByte().toInt()) {
                JS5_HIGH_PRIORITY_OPCODE, JS5_LOW_PRIORITY_OPCODE -> {
                    val uid = readChannel.readUMedium()
                    val indexId = uid shr 16
                    val groupId = uid and 0xffff
                    val masterRequest = indexId == 0xff && groupId == 0xff
                    ByteBuffer.wrap(if (masterRequest) checksums else store.groupReferenceTable(indexId, groupId)).apply {
                        if (capacity() == 0 || limit() == 0) return@withTimeout
                        val compression = if (masterRequest) 0 else get().toInt() and 0xff
                        val size = if (masterRequest) checksums.size else int
                        writeJS5File(indexId, groupId, compression, size, this)
                    }
                }
                JS5_ENCRYPTION_OPCODE -> readChannel.discard(3) // TODO
                JS5_LOGGED_IN_OPCODE, JS5_SWITCH_OPCODE -> readChannel.discard(3) // TODO
                else -> throw IllegalStateException("Unhandled Js5 opcode. Opcode=$opcode")
            }
        }
    }
} catch (exception: Exception) {
    handleException(exception)
}

private suspend fun Client.writeJS5File(indexId: Int, groupId: Int, compression: Int, size: Int, buffer: ByteBuffer) = writeChannel!!.apply {
    writeByte(indexId.toByte())
    writeShort(groupId.toShort())
    writeByte(compression.toByte())
    writeInt(size)

    var writeOffset = 8
    repeat(if (compression != 0) size + 4 else size) {
        if (writeOffset % 512 == 0) {
            writeByte(0xff.toByte())
            writeOffset = 1
        }
        writeByte(buffer[it + buffer.position()])
        writeOffset++
    }
}.flush()

private suspend fun Client.readLogin() {
    val readChannel = readChannel ?: return

    val opcode = readChannel.readByte().toInt() and 0xff

    val size = readChannel.readShort().toInt() and 0xffff
    val availableBytes = readChannel.availableForRead
    if (size != availableBytes) {
        writeResponse(BAD_SESSION_OPCODE)
        return disconnect("Bad session. Size Read=$size Available bytes=$availableBytes")
    }

    val majorVersion = readChannel.readInt()
    if (majorVersion != majorBuild) {
        writeResponse(CLIENT_OUTDATED_OPCODE)
        return disconnect("Client outdated. Major Version=$majorVersion")
    }

    val minorVersion = readChannel.readInt()
    if (minorVersion != minorBuild) {
        writeResponse(CLIENT_OUTDATED_OPCODE)
        return disconnect("Client outdated. Minor Version=$majorVersion")
    }

    readChannel.discard(1) // Unknown byte #1
    readChannel.discard(1) // Unknown byte #2

    when (opcode) {
        LOGIN_NORMAL_OPCODE -> {
            val rsa = ByteArray(readChannel.readShort().toInt() and 0xffff)
            if (readChannel.readAvailable(rsa, 0, rsa.size) != rsa.size) {
                writeResponse(BAD_SESSION_OPCODE)
                return disconnect("Bad session.")
            }
            val rsaBlock = ByteReadPacket(BigInteger(rsa).modPow(BigInteger(rsaExponent), BigInteger(rsaModulus)).toByteArray())
            if (!rsaBlock.readByte().toInt().toBoolean()) {
                writeResponse(BAD_SESSION_OPCODE)
                return disconnect("Bad session.")
            }
            val clientKeys = IntArray(4) { rsaBlock.readInt() }
            val clientSeed = rsaBlock.readLong()
            if (clientSeed != seed) {
                writeResponse(BAD_SESSION_OPCODE)
                return disconnect("Bad Session. Client/Server seed miss-match. ClientSeed=$clientSeed Seed=$seed")
            }
            when (val authenticationType = rsaBlock.readByte().toInt()) {
                1 -> rsaBlock.discard(4)
                0, 3 -> rsaBlock.discard(3)
                2 -> rsaBlock.discard(4)
                else -> {
                    writeResponse(BAD_SESSION_OPCODE)
                    return disconnect("Bad Session. Unhandled authentication type=$authenticationType")
                }
            }
            rsaBlock.discard(1) // Unknown byte #3
            val password = rsaBlock.readStringCp1252NullTerminated()
            rsaBlock.release()
            val xtea = ByteArray(readChannel.availableForRead)
            readChannel.readAvailable(xtea, 0, xtea.size)
            val xteaBlock = ByteReadPacket(xtea.fromXTEA(32, clientKeys))
            val username = xteaBlock.readStringCp1252NullTerminated()
            val clientSettings = xteaBlock.readByte().toInt()
            val clientResizeable = (clientSettings shr 1) == 1
            val clientWidth = xteaBlock.readUShort().toInt()
            val clientHeight = xteaBlock.readUShort().toInt()
            xteaBlock.discard(24)
            val token = xteaBlock.readStringCp1252NullTerminated()
            if (token != Client.token) {
                writeResponse(BAD_SESSION_OPCODE)
                return disconnect("Bad Session. Gamepack token is not valid. Token was $token.")
            }
            xteaBlock.discard(4) // Unknown Int #1
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
            xteaBlock.readUMedium()
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
            val clientType = xteaBlock.readUByte().toInt()
            val cacheCRCs = IntArray(store.validIndexCount()) { store.index(it).crc }
            val clientCRCs = IntArray(21) { -1 }
            if (xteaBlock.readInt() != 0 || xteaBlock.readInt() != 0) {
                writeResponse(BAD_SESSION_OPCODE)
                return disconnect("Bad session.")
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
            if (!IntArray(21) { store.index(it).crc }.contentEquals(clientCRCs)) {
                writeResponse(CLIENT_OUTDATED_OPCODE)
                return disconnect("Bad Session. Client and cache crc are mismatched.")
            }
            val serverKeys = IntArray(clientKeys.size) { clientKeys[it] + 50 }
            setIsaacCiphers(clientKeys.toISAAC(), serverKeys.toISAAC())

            PlayerDecoder.decodeFromJson(username, password).let {
                it.interfaces.currentInterfaceLayout = if (clientResizeable) InterfaceLayout.RESIZABLE else InterfaceLayout.FIXED
                this.player = it
                world.players.add(it)
            }.also { if (it) writeLogin(LOGIN_SUCCESS_OPCODE) else writeLogin(BAD_SESSION_OPCODE) }
        }
        else -> throw IllegalStateException("Unhandled login opcode $opcode")
    }
}

private suspend fun Client.writeLogin(response: Int) {
    writeResponse(response)
    if (response != LOGIN_SUCCESS_OPCODE) {
        return disconnect("Unsuccessful login.")
    }
    val player = player ?: return disconnect("Login write event does not have an established player.")

    writeChannel!!.apply {
        writeByte(11)
        writeByte(0)
        writeInt(0)
        writeByte(player.rights.toByte())
        writeByte(0)
        writeShort(player.index.toShort())
        writeByte(0)
    }.flush()

    world.requestLogin(player, this)
    readPackets(player)
}

private suspend fun Client.readPackets(player: Player) = try {
    while (true) {
        val readChannel = readChannel ?: break
        if (readChannel.isClosedForRead) break
        val opcode = readChannel.readPacketOpcode(clientCipher!!)
        if (opcode < 0 || opcode >= sizes.size) continue
        val size = readChannel.readPacketSize(sizes[opcode])
        // Take the bytes from the read channel before doing any checks.
        val packet = readChannel.readPacket(size)
        val disassembler = PacketDisassemblerListener.listeners.entries.firstOrNull { it.key == opcode }
        if (disassembler == null) {
            logger.debug { "No packet disassembler found for packet opcode $opcode." }
            continue
        }
        if (disassembler.value.size != -1 && disassembler.value.size != size) {
            logger.debug { "Packet disassembler size is not equal to the packet array size. Disassembler size was ${disassembler.value.size} and found size was $size." }
            continue
        }
        val disassembled = PacketDisassemblerListener.listeners[disassembler.key]?.packet?.invoke(packet)
        if (disassembled == null) {
            logger.debug { "Disassembled packet returned null. Opcode was $opcode." }
            continue
        }
        val handler = PacketHandlerListener.listeners[disassembled::class]
        if (handler == null) {
            logger.debug { "No packet handler found for disassembled packet. Opcode was $opcode." }
            continue
        }
        handler.invoke(PacketHandler(player, disassembled))
    }
} catch (exception: Exception) {
    handleException(exception)
}
