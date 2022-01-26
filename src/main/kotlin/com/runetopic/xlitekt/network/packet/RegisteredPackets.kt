package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.network.packet.assembler.* // ktlint-disable no-unused-imports
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
        IfSetColorPacket::class to IfSetColorPacketAssembler(),
        LogoutPacket::class to LogoutPacketAssembler(),
        MessageGamePacket::class to MessageGamePacketAssembler(),
        MidiSongPacket::class to MidiSongPacketAssembler(),
        NoTimeoutPacket::class to NoTimeoutPacketAssembler(),
        PlayerInfoPacket::class to PlayerInfoPacketAssembler(),
        RebuildNormalPacket::class to RebuildNormalPacketAssembler(),
        SetMapFlagPacket::class to SetMapFlagPacketAssembler(),
        UpdateContainerFullPacket::class to UpdateContainerFullPacketAssembler(),
        UpdateRunEnergyPacket::class to UpdateRunEnergyAssembler(),
        UpdateStatPacket::class to UpdateStatAssembler(),
        UpdatePublicChatStatusPacket::class to UpdatePublicChatStatusPacketAssembler(),
        UpdatePrivateChatStatusPacket::class to UpdatePrivateChatStatusPacketAssembler(),
        UpdateWeightPacket::class to UpdateWeightPacketAssembler()
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
