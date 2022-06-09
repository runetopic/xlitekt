package script.packet.assembler

import xlitekt.game.packet.UpdateFriendListPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocateDynamic
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateFriendListPacket>(opcode = 38, size = -2) {
    allocateDynamic(256) {
        for (friend in friends) {
            writeByte(friend.visible::toInt)
            writeStringCp1252NullTerminated(friend::displayName)
            writeStringCp1252NullTerminated { "" }
            writeShort(friend.visible::toInt)
            writeByte { 0x2 }
            writeByte { 0x1 }
            if (friend.visible) {
                writeStringCp1252NullTerminated { "" }
                writeByte { 1 }
                writeInt { 1 }
            }
            writeStringCp1252NullTerminated { "" }
        }
    }
}
