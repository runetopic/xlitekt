package script.packet.assembler

import xlitekt.game.packet.UpdateFriendListPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeStringCp1252NullTerminated
import xlitekt.shared.lazyInject
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketAssemblerListener>().assemblePacket<UpdateFriendListPacket>(opcode = 38, size = -2) {
    for (friend in friends) {
        it.writeByte(friend.visible.toInt())
        it.writeStringCp1252NullTerminated(friend.displayName)
        it.writeStringCp1252NullTerminated("")
        it.writeShort(friend.visible.toInt())
        it.writeByte(0x2)
        it.writeByte(0x1)
        if (friend.visible) {
            it.writeStringCp1252NullTerminated("")
            it.writeByte(1)
            it.writeInt(1)
        }
        it.writeStringCp1252NullTerminated("")
    }
}
