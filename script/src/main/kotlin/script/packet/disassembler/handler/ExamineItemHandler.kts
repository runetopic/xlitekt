import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineItemPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject
import xlitekt.shared.resource.ItemExamines

private val logger = InlineLogger()
private val npcExamines by inject<ItemExamines>()

onPacketHandler<ExamineItemPacket> {
    val neighboringItems = player.zone()?.neighboringItems()

    if (neighboringItems?.isEmpty() == true || neighboringItems?.none { it.id == packet.itemID } == true) {
        logger.debug { "Item-Examine NPC id ${packet.itemID} not found" }
        player.message { "Item id ${packet.itemID} not found." }
        return@onPacketHandler
    }

    val examine = npcExamines[packet.itemID] ?: return@onPacketHandler
    if (examine.message.isEmpty()) return@onPacketHandler

    player.message { examine.message }
}
