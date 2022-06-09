package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.actor.angleTo
import xlitekt.game.actor.cancelWeak
import xlitekt.game.actor.routeTo
import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler

private val logger = InlineLogger()

onPacketHandler<OpNPCPacket> {
    val neighboringNpcs = player.zone().neighboringNpcs()

    if (neighboringNpcs.isEmpty() || neighboringNpcs.none { it.index == packet.npcIndex }) {
        logger.debug { "World does not contain NPC Index = ${packet.npcIndex}" }
        return@onPacketHandler
    }

    val npc = neighboringNpcs.firstOrNull { it.index == this.packet.npcIndex } ?: return@onPacketHandler

    if (npc.entry == null) {
        logger.debug { "Invalid NPC clicked $npc" }
        return@onPacketHandler
    }

    with(player) {
        cancelWeak()
        routeTo(npc) {
            angleTo(npc)
        }
    }
}
