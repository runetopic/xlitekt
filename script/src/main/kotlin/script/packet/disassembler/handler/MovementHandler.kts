package script.packet.disassembler.handler

import xlitekt.game.actor.cancelAll
import xlitekt.game.actor.routeTo
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.Location

onPacketHandler<MovementPacket> {
    val destination = Location(packet.destinationX, packet.destinationZ, player.location.level)
    with(player) {
        cancelAll()
        routeTo(destination)
    }
}
