package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.game.actor.faceActor
import xlitekt.game.actor.route
import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.Zone
import xlitekt.shared.inject

private val logger = InlineLogger()
private val zoneFlags by inject<ZoneFlags>()
private val world by inject<World>()
private val pathfinder = SmartPathFinder(
    flags = zoneFlags.flags,
    defaultFlag = 0,
    useRouteBlockerFlags = false,
)

onPacketHandler<OpNPCPacket> {
    if (world.npcs().indices.none { it == packet.npcIndex }) {
        logger.debug { "World does not contain NPC Index = ${packet.npcIndex}" }
        return@onPacketHandler
    }

    val npc = player.zones().flatMap(Zone::npcs).firstOrNull { it.index == this.packet.npcIndex } ?: return@onPacketHandler

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

    player.route { path.coords.map { Location(it.x, it.y, npc.location.level) } }
    player.faceActor(npc::index) // this may need to have some sort of requirement like within distance checks or not.
}
