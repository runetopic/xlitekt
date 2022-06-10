package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.game.actor.angleTo
import xlitekt.game.actor.cancelAll
import xlitekt.game.actor.queueStrong
import xlitekt.game.actor.routeTo
import xlitekt.game.packet.OpLocPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.world.map.Location
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()
private val locEntryTypeProvider by inject<LocEntryTypeProvider>()

onPacketHandler<OpLocPacket> {
    val locId = packet.locId
    val x = packet.x
    val z = packet.z
    val running = packet.isModified

    if (!locEntryTypeProvider.exists(locId)) {
        logger.debug { "Invalid loc op objectId=$locId, x=$x, z=$z, running=$running" }
        return@onPacketHandler
    }

    logger.debug { "Clicked loc op objectId=$locId, x=$x, z=$z, running=$running" }

    // The objects in our zone.
    val objects = player.zone().neighboringLocs()

    // Server check if this zone objects contains the clicked object id.
    if (objects.none { it.id == locId }) return@onPacketHandler

    // The location of the object clicked.
    val location = Location(x, z, player.location.level)

    val gameObject = objects.firstOrNull {
        it.id == locId && it.location.packedLocation == location.packedLocation
    } ?: return@onPacketHandler

//    // Teleport movement (ctrl+click teleporting)
//    if (player.rights >= 2 && packet.isModified) {
//        player.teleportTo { waypoints.last }
//        return@onPacketHandler
//    }
//
//    // Toggles Actor's speed only for the duration of the movement (if isModified=true)
//    player.speed { (VarPlayer.ToggleRun in player.vars).let { if (packet.isModified) !it else it } }

    val action: () -> Unit =
        if (gameObject.id == 409) {
            { player.prayer.prayAtAltar(gameObject) }
        } else {
            { player.angleTo(gameObject) }
        }

    with(player) {
        cancelAll()
        queueStrong {
            routeTo(gameObject) {
                angleTo(gameObject)
            }
        }
    }
}
