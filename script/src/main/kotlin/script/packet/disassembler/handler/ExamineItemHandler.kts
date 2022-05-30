package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineItemPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject
import xlitekt.shared.resource.ItemExamines

private val logger = InlineLogger()
private val itemExamines by inject<ItemExamines>()
private val provider by inject<ObjEntryTypeProvider>()

/**
 * @author Justin Kenney
 */
onPacketHandler<ExamineItemPacket> {
    if (!provider.exists(packet.itemID)) return@onPacketHandler

    val neighboringItems = player.zone()?.neighboringItems()

    if (neighboringItems?.isEmpty() == true || neighboringItems?.none { it.id == packet.itemID } == true) {
        logger.debug { "Item-Examine Item id ${packet.itemID} not found" }
        player.message { "Item id ${packet.itemID} not found." }
        return@onPacketHandler
    }

    val examine = itemExamines[packet.itemID] ?: return@onPacketHandler
    if (examine.message.isEmpty()) return@onPacketHandler

    player.message { examine.message }
}
