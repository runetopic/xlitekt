package com.runetopic.xlitekt.network.packet

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.map.location.Location
import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val player: Player,
    val updates: Map<Player, ByteReadPacket>,
    val locations: Map<Player, Location>
) : Packet
