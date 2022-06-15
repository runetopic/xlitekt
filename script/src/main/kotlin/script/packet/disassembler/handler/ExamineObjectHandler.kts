package script.packet.disassembler.handler

import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineObjectPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject
import xlitekt.shared.resource.ObjectExamines

private val provider by inject<LocEntryTypeProvider>()
private val objectExamines by inject<ObjectExamines>()

/**
 * @author Justin Kenney
 */
onPacketHandler<ExamineObjectPacket> {
    if (!provider.exists(packet.objectID)) return@onPacketHandler
    val examine = objectExamines[packet.objectID] ?: return@onPacketHandler
    if (examine.message.isEmpty()) return@onPacketHandler

    // The objects in our zone.
    val objects = player.zone.neighboringLocs()

    // Server check if this zone objects contains the clicked object id.
    if (objects.none { it.id == packet.objectID }) return@onPacketHandler

    val gameObject = objects.firstOrNull {
        it.id == packet.objectID
    } ?: return@onPacketHandler

//    if (!player.examine(gameObject))
    player.message { examine.message }
}
