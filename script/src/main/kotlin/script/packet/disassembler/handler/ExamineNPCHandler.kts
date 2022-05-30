import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineNPCPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject
import xlitekt.shared.resource.NPCExamines

private val logger = InlineLogger()
private val npcExamines by inject<NPCExamines>()

onPacketHandler<ExamineNPCPacket> {
    val neighboringNpcs = player.zone()?.neighboringNpcs()

    if (neighboringNpcs?.isEmpty() == true || neighboringNpcs?.none { it.id == packet.npcID } == true) {
        logger.debug { "NPC-Examine NPC id ${packet.npcID} not found" }
        player.message { "NPC id ${packet.npcID} not found." }
        return@onPacketHandler
    }

    val examine = npcExamines[packet.npcID] ?: return@onPacketHandler
    if (examine.message.isEmpty()) return@onPacketHandler

    player.message { examine.message }
}
