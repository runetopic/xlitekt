package script.packet.disassembler.handler

import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject

private val zoneFlags by inject<ZoneFlags>()

val pf = SmartPathFinder(
    flags = zoneFlags.flags,
    defaultFlag = 0
)

onPacketHandler<MovementPacket> {
    val path = pf.findPath(
        srcX = player.location.x,
        srcY = player.location.z,
        destX = packet.destinationX,
        destY = packet.destinationZ,
        z = player.location.level
    )
    player.movement.route(path.coords.map { Location(it.x, it.y, player.location.level) })
}
