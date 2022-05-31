package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineNPCPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject
import xlitekt.shared.resource.NPCExamines

private val logger = InlineLogger()
private val npcExamines by inject<NPCExamines>()
private val provider by inject<NPCEntryTypeProvider>()

/**
 * @author Justin Kenney
 */
onPacketHandler<ExamineNPCPacket> {
    if (!provider.exists(packet.npcId)) return@onPacketHandler
    val neighboringNpcs = player.zone()?.neighboringNpcs()

    if (neighboringNpcs?.isEmpty() == true) {
        logger.debug { "NPC-Examine NPC id ${packet.npcId} not found" }
        player.message { "NPC id ${packet.npcId} not found." }
        return@onPacketHandler
    }

    val examine = npcExamines[packet.npcId] ?: return@onPacketHandler
    if (examine.message.isEmpty()) return@onPacketHandler

    player.message { examine.message }
}
