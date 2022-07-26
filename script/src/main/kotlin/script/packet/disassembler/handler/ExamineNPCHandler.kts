package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineNPCPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.inject
import xlitekt.shared.insert
import xlitekt.shared.resource.NPCExamines

private val logger = InlineLogger()
private val npcExamines by inject<NPCExamines>()
private val provider by inject<NPCEntryTypeProvider>()

/**
 * @author Justin Kenney
 */
insert<PacketHandlerListener>().handlePacket<ExamineNPCPacket> {
    if (!provider.exists(packet.npcId)) return@handlePacket
    val neighboringNpcs = player.zone.neighboringNpcs()

    if (neighboringNpcs.isEmpty()) {
        logger.debug { "NPC-Examine NPC id ${packet.npcId} not found" }
        player.message { "NPC id ${packet.npcId} not found." }
        return@handlePacket
    }

    val examine = npcExamines[packet.npcId] ?: return@handlePacket
    if (examine.message.isEmpty()) return@handlePacket

    player.message(examine::message)
}
