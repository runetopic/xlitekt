package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import it.unimi.dsi.fastutil.ints.IntArrayList
import it.unimi.dsi.fastutil.ints.IntList
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.game.actor.route
import xlitekt.game.actor.routeTeleport
import xlitekt.game.actor.speed
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.packet.OpLocPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.World
import xlitekt.game.world.map.Location
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()
private val locEntryTypeProvider by inject<LocEntryTypeProvider>()
private val zoneFlags by inject<ZoneFlags>()
private val pathfinder = SmartPathFinder(
    flags = zoneFlags.flags,
    defaultFlag = 0,
    useRouteBlockerFlags = true
)
private val world by inject<World>()

onPacketHandler<OpLocPacket> {
    val objectId = packet.objectId
    val x = packet.x
    val z = packet.z
    val isModified = packet.isModified

    logger.debug { packet }

    if (locEntryTypeProvider.entryType(objectId) == null) {
//        logger.debug { "Invalid loc op objectId=$objectId, x=$x, z=$z, isModifiedClick=$isModified" }
        return@onPacketHandler
    }

//    logger.debug { "Clicked loc op objectId=$objectId, x=$x, z=$z, isModifiedClick=$isModifiedClick" }

    // The location of the object clicked.
    val location = Location(x, z, player.location.level)
    // The objects in our zone.
    val objects = player.zone().neighboringLocs()
    // Server check if this zone objects contains the clicked object id.
    if (objects.none { it.id == objectId }) return@onPacketHandler

    val gameObject = objects.firstOrNull {
        it.id == objectId && it.location.packedLocation == location.packedLocation
    } ?: return@onPacketHandler

    val path = pathfinder.findPath(
        srcX = player.location.x,
        srcY = player.location.z,
        destX = location.x,
        destY = location.z,
        destWidth = gameObject.entry.width,
        destHeight = gameObject.entry.height,
        objRot = gameObject.rotation,
        objShape = gameObject.shape,
        z = location.level
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
        path.coords.forEach { list.add(Location(it.x, it.y, location.level).packedLocation) }
        list
    }
}
