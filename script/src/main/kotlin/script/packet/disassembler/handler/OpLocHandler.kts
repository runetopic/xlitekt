package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import org.rsmod.pathfinder.SmartPathFinder
import org.rsmod.pathfinder.ZoneFlags
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.game.packet.OpLocPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.Zones
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()
private val locEntryTypeProvider by inject<LocEntryTypeProvider>()
private val zoneFlags by inject<ZoneFlags>()

onPacketHandler<OpLocPacket> {
    val objectId = packet.objectId
    val x = packet.x
    val z = packet.z
    val running = packet.running

    if (locEntryTypeProvider.entryType(objectId) == null) {
        logger.debug { "Invalid loc op objectId=$objectId, x=$x, z=$z, running=$running" }
        return@onPacketHandler
    }

    logger.debug { "Clicked loc op objectId=$objectId, x=$x, z=$z, running=$running" }

    // The location of the object clicked.
    val location = Location(x, z, player.location.level)
    // The zone the object location is in.
    val zone = Zones[location] ?: return@onPacketHandler
    // The objects in this zone.
    val objects = zone.gameObjects
    // Server check if this zone objects contains the clicked object id.
    if (objects.none { it?.id == objectId }) return@onPacketHandler

    val gameObject = objects.firstOrNull {
        it?.id == objectId && it.location.packedLocation == location.packedLocation
    } ?: return@onPacketHandler

    val pf = SmartPathFinder(
        flags = zoneFlags.flags,
        defaultFlag = 0,
        useRouteBlockerFlags = true
    ).findPath(
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
    player.movement.route(pf.coords.map { Location(it.x, it.y, location.level) })
}
