package script.packet.disassembler.handler

import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineItemPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject
import xlitekt.shared.resource.ItemExamines

private val itemExamines by inject<ItemExamines>()
private val provider by inject<ObjEntryTypeProvider>()

/**
 * @author Justin Kenney
 */
onPacketHandler<ExamineItemPacket> {
    if (!provider.exists(packet.itemId)) return@onPacketHandler

    val examine = itemExamines[packet.itemId] ?: return@onPacketHandler
    if (examine.message.isEmpty()) return@onPacketHandler

    player.message { examine.message }
}
