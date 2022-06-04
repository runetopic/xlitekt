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
    // Teleport movement (ctrl+click teleporting)
    if (player.rights >= 2 && packet.isModified) {
        val destination = Location(packet.destinationX, packet.destinationZ, player.location.level)
        player.routeTeleport { destination }
        return@onPacketHandler
    }

    // Toggles Actor's speed only for the duration of the movement (if isModified=true)
    player.speed { (VarPlayer.ToggleRun in player.vars).let { if (packet.isModified) !it else it } }

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
