package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.game.actor.angleTo
import xlitekt.game.actor.cancelAll
import xlitekt.game.actor.queueStrong
import xlitekt.game.actor.routeTo
import xlitekt.game.content.interact.interact
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
    val running = packet.running

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

    val clickedOption = gameObject.entry?.actions?.firstOrNull() ?: "*"

    with(player) {
        cancelAll()
        queueStrong {
            routeTo(gameObject) {
                angleTo(gameObject)
                interact(clickedOption, gameObject)
            }
        }
    }
}
