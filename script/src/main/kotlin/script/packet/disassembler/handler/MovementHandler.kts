package script.packet.disassembler.handler

import org.rsmod.pathfinder.SmartPathFinder
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.ZoneFlags


onPacketHandler<MovementPacket> {
    val path = SmartPathFinder(
        flags = ZoneFlags.flags,
        defaultFlag = 0
    ).findPath(
        srcX = player.location.x,
        srcY = player.location.z,
        destX = packet.destinationX,
        destY = packet.destinationZ,
        z = player.location.level
    )

    player.movement.reset()
    player.movement.addAll(path.coords.map { Location(it.x, it.y, player.location.level) })
}
