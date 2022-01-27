package com.runetopic.xlitekt.network.packet.handler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.OpHeldPacket

/**
 * @author Jordan Abraham
 */
class OpHeldPacketHandler : PacketHandler<OpHeldPacket> {

    private val logger = InlineLogger()

    override suspend fun handlePacket(player: Player, packet: OpHeldPacket) {
        val index = packet.index
        val fromPackedInterface = packet.fromPackedInterface
        val fromInterfaceId = fromPackedInterface shr 16
        val fromChildId = fromPackedInterface and 0xffff
        val fromSlotId = packet.fromSlotId
        val fromItemId = packet.fromItemId

        val toPackedInterface = packet.toPackedInterface
        val toInterfaceId = toPackedInterface shr 16
        val toChildId = toPackedInterface and 0xffff
        val toSlotId = packet.toSlotId
        val toItemId = packet.toItemId

        logger.info { "Clicked op held fromInterfaceId=$fromInterfaceId, toInterfaceId=$toInterfaceId, fromChildId=$fromChildId, toChildId=$toChildId, fromSlotId=$fromSlotId, toSlotId=$toSlotId, fromItemId=$fromItemId, toItemId=$toItemId, index=$index" }
    }
}
