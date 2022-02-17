package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateFriendListPacket
import com.runetopic.xlitekt.shared.buffer.writeStringCp1252NullTerminated
import com.runetopic.xlitekt.shared.toByte
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort

/**
 * @author Tyler Telis
 */
class UpdateFriendsListPacketAssembler : PacketAssembler<UpdateFriendListPacket>(opcode = 38, size = -2) {
    override fun assemblePacket(packet: UpdateFriendListPacket) = buildPacket {
        for (friend in packet.friends) {
            writeByte(friend.visible.toByte())
            writeStringCp1252NullTerminated(friend.displayName)
            writeStringCp1252NullTerminated("")
            writeShort(friend.visible.toByte().toShort())
            writeByte(0x2)
            writeByte(0x1)
            if (friend.visible) {
                writeStringCp1252NullTerminated("")
                writeByte(1)
                writeInt(1)
            }
            writeStringCp1252NullTerminated("")
        }
    }
}
