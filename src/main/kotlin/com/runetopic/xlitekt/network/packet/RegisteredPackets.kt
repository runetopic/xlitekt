package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.network.packet.assembler.CamResetPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.ForceLogoutPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfCloseSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfMoveSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfOpenSubPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfOpenTopPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfSetColorPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.IfSetTextPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.MessageGamePacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.MidiSongPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.NoTimeoutPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.PlayerInfoPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.RebuildNormalPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.SetMapFlagPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.SetPlayerOpPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateContainerFullPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateContainerPartialPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdatePrivateChatStatusPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdatePublicChatStatusPacketAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateRunEnergyAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateStatAssembler
import com.runetopic.xlitekt.network.packet.assembler.UpdateWeightPacketAssembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton1PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton2PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton3PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton4PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton5PacketDisassembler
import com.runetopic.xlitekt.network.packet.disassembler.IfButton6PacketDisassembler
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
    )

    val handlers = mapOf(
        IfButtonPacket::class to IfButtonPacketHandler()
    )
}
