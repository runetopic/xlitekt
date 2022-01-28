package com.runetopic.xlitekt.network.packet.handler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.OpObjPacket

/**
 * @author Jordan Abraham
 */
class OpObjPacketHandler : PacketHandler<OpObjPacket> {

    private val logger = InlineLogger()

    override suspend fun handlePacket(player: Player, packet: OpObjPacket) {
        val index = packet.index
        val packedInterface = packet.packedInterface
        val interfaceId = packedInterface shr 16
        val childId = packedInterface and 0xffff
        val slotId = packet.slotId
        val itemId = packet.itemId
        logger.debug { "Clicked obj op interfaceId=$interfaceId, childId=$childId, slotId=$slotId, itemId=$itemId, index=$index" }
    }
}
