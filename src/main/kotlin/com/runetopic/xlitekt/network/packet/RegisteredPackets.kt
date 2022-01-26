package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.network.packet.assembler.IfCloseSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfMoveSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfOpenSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfOpenTopPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.LogoutPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.MessageGamePacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.NoTimeoutPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.PlayerInfoPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.RebuildNormalPacketAssembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton1PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.PingStatisticsPacketDisassembler
import com.runetopic.xlitekt.network.packet.handler.IfButton1PacketHandler
import com.runetopic.xlitekt.network.packet.handler.PingStatisticsPacketHandler

/**
 * @author Jordan Abraham
 */
object RegisteredPackets {
    val assemblers = mapOf(
        IfOpenTopPacket::class to IfOpenTopPacketAssembler(),
        IfOpenSubPacket::class to IfOpenSubPacketAssembler(),
        IfMoveSubPacket::class to IfMoveSubPacketAssembler(),
        IfCloseSubPacket::class to IfCloseSubPacketAssembler(),
        LogoutPacket::class to LogoutPacketAssembler(),
        MessageGamePacket::class to MessageGamePacketAssembler(),
        NoTimeoutPacket::class to NoTimeoutPacketAssembler(),
        PlayerInfoPacket::class to PlayerInfoPacketAssembler(),
        RebuildNormalPacket::class to RebuildNormalPacketAssembler(),
    )

    val disassemblers = setOf(
        PingStatisticsPacketDisassembler(),
        IfButton1PacketDisassembler()
    )

    val handlers = mapOf(
        PingStatisticsPacket::class to PingStatisticsPacketHandler(),
        IfButtonPacket::class to IfButton1PacketHandler()
    )
}
