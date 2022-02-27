package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateFriendListPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeStringCp1252NullTerminated
import com.runetopic.xlitekt.shared.toByte
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort

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
