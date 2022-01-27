package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.network.packet.assembler.*
import com.runetopic.xlitekt.network.packet.disassembler.IfButton10PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton1PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton2PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton3PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton4PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton5PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton6PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton7PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton8PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton9PacketDisassembler
import com.runetopic.xlitekt.network.packet.handler.IfButtonPacketHandler

/**
 * @author Jordan Abraham
 */
object RegisteredPackets {
    val assemblers = mapOf(
        CamResetPacket::class to CamResetPacketAssembler(),
        IfOpenTopPacket::class to IfOpenTopPacketAssembler(),
        IfOpenSubPacket::class to IfOpenSubPacketAssembler(),
        IfMoveSubPacket::class to IfMoveSubPacketAssembler(),
        IfCloseSubPacket::class to IfCloseSubPacketAssembler(),
        IfSetColorPacket::class to IfSetColorPacketAssembler(),
        IfSetTextPacket::class to IfSetTextPacketAssembler(),
        ForceLogoutPacket::class to ForceLogoutPacketAssembler(),
        MessageGamePacket::class to MessageGamePacketAssembler(),
        MidiSongPacket::class to MidiSongPacketAssembler(),
        NoTimeoutPacket::class to NoTimeoutPacketAssembler(),
        PlayerInfoPacket::class to PlayerInfoPacketAssembler(),
        RebuildNormalPacket::class to RebuildNormalPacketAssembler(),
        SetMapFlagPacket::class to SetMapFlagPacketAssembler(),
        SetPlayerOpPacket::class to SetPlayerOpPacketAssembler(),
        UpdateContainerFullPacket::class to UpdateContainerFullPacketAssembler(),
        UpdateContainerPartialPacket::class to UpdateContainerPartialPacketAssembler(),
        UpdateFriendListPacket::class to UpdateFriendsListPacketAssembler(),
        UpdateRunEnergyPacket::class to UpdateRunEnergyAssembler(),
        UpdateStatPacket::class to UpdateStatAssembler(),
        UpdatePublicChatStatusPacket::class to UpdatePublicChatStatusPacketAssembler(),
        UpdatePrivateChatStatusPacket::class to UpdatePrivateChatStatusPacketAssembler(),
        UpdateWeightPacket::class to UpdateWeightPacketAssembler(),
    )

    val disassemblers = setOf(
        IfButton1PacketDisassembler(),
        IfButton2PacketDisassembler(),
        IfButton3PacketDisassembler(),
        IfButton4PacketDisassembler(),
        IfButton5PacketDisassembler(),
        IfButton6PacketDisassembler(),
        IfButton7PacketDisassembler(),
        IfButton8PacketDisassembler(),
        IfButton9PacketDisassembler(),
        IfButton10PacketDisassembler(),
    )

    val handlers = mapOf(
        IfButtonPacket::class to IfButtonPacketHandler()
    )
}
