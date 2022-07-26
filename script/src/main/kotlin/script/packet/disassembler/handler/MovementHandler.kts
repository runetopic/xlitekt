package script.packet.disassembler.handler

import xlitekt.game.actor.cancelAll
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.queueStrong
import xlitekt.game.actor.routeTo
import xlitekt.game.packet.MoveGameClickPacket
import xlitekt.game.packet.MoveMinimapClickPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.game.world.map.Location
import xlitekt.shared.insert

insert<PacketHandlerListener>().handlePacket<MoveGameClickPacket> {
    player.queueRoute(Location(packet.destinationX, packet.destinationZ, player.location.level))
}

insert<PacketHandlerListener>().handlePacket<MoveMinimapClickPacket> {
    player.queueRoute(Location(packet.destinationX, packet.destinationZ, player.location.level))
}

fun Player.queueRoute(destination: Location) {
    cancelAll()
    queueStrong {
        routeTo(destination)
    }
}
