package script.packet.disassembler.handler

import xlitekt.game.actor.cancelAll
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.queueStrong
import xlitekt.game.actor.routeTo
import xlitekt.game.packet.MoveGameClickPacket
import xlitekt.game.packet.MoveMinimapClickPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.Location

onPacketHandler<MoveGameClickPacket> {
    player.queueRoute(Location(packet.destinationX, packet.destinationZ, player.location.level))
}

onPacketHandler<MoveMinimapClickPacket> {
    player.queueRoute(Location(packet.destinationX, packet.destinationZ, player.location.level))
}

fun Player.queueRoute(destination: Location) {
    cancelAll()
    queueStrong {
        routeTo(destination)
    }
}
