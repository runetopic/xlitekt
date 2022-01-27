package com.runetopic.xlitekt.network.packet.handler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.IfButtonPacket

/**
 * @author Jordan Abraham
 */
class IfButton1PacketHandler : PacketHandler<IfButtonPacket> {

    private val logger = InlineLogger()

    override suspend fun handlePacket(player: Player, message: IfButtonPacket) {
        logger.info { "IfButton1 Handler." }
    }
}
