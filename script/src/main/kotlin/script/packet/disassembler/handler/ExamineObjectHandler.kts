import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineObjectPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject
import xlitekt.shared.resource.ObjectExamines

private val logger = InlineLogger()
private val objectExamines by inject<ObjectExamines>()

onPacketHandler<ExamineObjectPacket> {
    val neighboringObjects = player.zone()?.neighboringObjects()

    if (neighboringObjects?.isEmpty() == true || neighboringObjects?.none { it.id == packet.objectID } == true) {
        logger.debug { "Object-Examine Object id ${packet.objectID} not found" }
        player.message { "Object id ${packet.objectID} not found." }
        return@onPacketHandler
    }

    val examine = objectExamines[packet.objectID] ?: return@onPacketHandler
    if (examine.message.isEmpty()) return@onPacketHandler

    player.message { examine.message }
}
