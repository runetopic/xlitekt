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
    player.message { examine.message }
}
