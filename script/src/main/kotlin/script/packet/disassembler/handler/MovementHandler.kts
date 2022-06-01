package script.packet.disassembler.handler

import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.route
import xlitekt.game.actor.routeTeleport
import xlitekt.game.actor.speed
import xlitekt.game.content.vars.VarPlayer
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
    // Teleport movement
    if (player.rights >= 2 && packet.movementType == 1) {
        val destination = Location(packet.destinationX, packet.destinationZ, player.location.level)
        player.routeTeleport { destination }
        return@onPacketHandler
    }

    // Toggles Actor's speed only for the duration of the movement (if movementType=1)
    val currentSpeed = VarPlayer.ToggleRun in player.vars
    player.speed { currentSpeed }
    if (packet.movementType == 1) {
        player.speed { !currentSpeed }
    }

    // Normal movement
    val path = pathfinder.findPath(
        srcX = player.location.x,
        srcY = player.location.z,
        destX = packet.destinationX,
        destY = packet.destinationZ,
        z = player.location.level
    )

    player.route {
        val list: IntList = IntArrayList(path.coords.size)
        path.coords.forEach { list.add(Location(it.x, it.y, player.location.level).packedLocation) }
        list
    }
}
