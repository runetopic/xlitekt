package script.packet.disassembler.handler

import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.route
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject

private val zoneFlags by inject<ZoneFlags>()
private val pathfinder = SmartPathFinder(
    flags = zoneFlags.flags,
    defaultFlag = 0
)

onPacketHandler<MovementPacket> {
    val path = pathfinder.findPath(
        srcX = player.location.x,
        srcY = player.location.z,
        destX = packet.destinationX,
        destY = packet.destinationZ,
        z = player.location.level
    )
    player.route { path.coords.map { Location(it.x, it.y, player.location.level) } }
}
