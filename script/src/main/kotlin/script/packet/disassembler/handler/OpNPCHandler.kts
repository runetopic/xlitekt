package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.faceActor
import xlitekt.game.actor.route
import xlitekt.game.actor.routeTeleport
import xlitekt.game.actor.speed
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.Location
import xlitekt.shared.inject

private val logger = InlineLogger()
private val zoneFlags by inject<ZoneFlags>()
private val pathfinder = SmartPathFinder(
    flags = zoneFlags.flags,
    defaultFlag = 0,
    useRouteBlockerFlags = false,
)

onPacketHandler<OpNPCPacket> {
    val neighboringNpcs = player.zone().neighboringNpcs()

    logger.debug { packet }

    println(neighboringNpcs.indices.contains(packet.npcIndex))

    if (neighboringNpcs.isEmpty() || neighboringNpcs.indices.none { it == packet.npcIndex }) {
        logger.debug { "World does not contain NPC Index = ${packet.npcIndex}" }
        return@onPacketHandler
    }

    val npc = neighboringNpcs.firstOrNull { it.index == this.packet.npcIndex } ?: return@onPacketHandler

    if (npc.entry == null) {
        logger.debug { "Invalid NPC clicked $npc" }
        return@onPacketHandler
    }

    val path = pathfinder.findPath(
        srcX = player.location.x,
        srcY = player.location.z,
        srcSize = 1,
        destX = npc.location.x,
        destY = npc.location.z,
        destWidth = npc.entry!!.size,
        destHeight = npc.entry!!.size,
        objShape = -2,
        z = npc.location.level,
    )

    // ctrl+click teleporting to object
    if (player.rights >= 2 && packet.isModified) {
        val destination = path.coords.last()
        player.routeTeleport { Location(destination.x, destination.y, player.location.level) }
        return@onPacketHandler
    }

    // Toggles Actor's speed only for the duration of the movement (if isModified=true)
    player.speed { (VarPlayer.ToggleRun in player.vars).let { if (packet.isModified) !it else it } }

    player.route {
        val list: IntList = IntArrayList(path.coords.size)
        path.coords.forEach { list.add(Location(it.x, it.y, npc.location.level).packedLocation) }
        list
    }
    player.faceActor(npc::index) // this may need to have some sort of requirement like within distance checks or not.
}
