package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import xlitekt.game.packet.UpdateFriendListPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.toByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateFriendListPacket>(opcode = 38, size = -2) {
    buildPacket {
        friends.forEach {
            writeByte(it.visible.toByte())
            writeStringCp1252NullTerminated(it.displayName)
            writeStringCp1252NullTerminated("")
            writeShort(it.visible.toByte().toShort())
            writeByte(0x2)
            writeByte(0x1)
            if (it.visible) {
                writeStringCp1252NullTerminated("")
                writeByte(1)
                writeInt(1)
            }
            writeStringCp1252NullTerminated("")
        }
    }
}
