package com.runetopic.xlitekt.network.packet.handler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.NoTimeoutPacket
import com.runetopic.xlitekt.network.packet.PingStatisticsPacket

/**
 * @author Jordan Abraham
 */
class PingStatisticsPacketHandler : PacketHandler<PingStatisticsPacket> {

    private val logger = InlineLogger()

    override suspend fun handlePacket(player: Player, message: PingStatisticsPacket) {
        logger.info { "Handling ping statistics packet decoder." }
        player.client.writePacket(NoTimeoutPacket())
    }
}
