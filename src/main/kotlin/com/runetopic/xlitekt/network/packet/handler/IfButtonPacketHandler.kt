package com.runetopic.xlitekt.network.packet.handler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.IfButtonPacket

/**
 * @author Jordan Abraham
 */
class IfButtonPacketHandler : PacketHandler<IfButtonPacket> {

    private val logger = InlineLogger()

    override suspend fun handlePacket(player: Player, packet: IfButtonPacket) {
        val index = packet.index
        val interfaceId = packet.packedInterface shr 16
        val childId = packet.packedInterface and 0xffff
        val slotId = packet.slotId
        val itemId = packet.itemId
        logger.info { "Clicked interfaceId=$interfaceId, childId=$childId, slotId=$slotId, itemId=$itemId, index=$index" }
    }
}
