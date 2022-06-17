package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.actor.angleTo
import xlitekt.game.actor.cancelAll
import xlitekt.game.actor.faceActor
import xlitekt.game.actor.queueStrong
import xlitekt.game.actor.routeTo
import xlitekt.game.packet.OpNPCPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.lazyInject

private val logger = InlineLogger()

lazyInject<PacketHandlerListener>().handlePacket<OpNPCPacket> {
    val neighboringNpcs = player.zone.neighboringNpcs()

    if (neighboringNpcs.isEmpty() || neighboringNpcs.none { it.index == packet.npcIndex }) {
        logger.debug { "World does not contain NPC Index = ${packet.npcIndex}" }
        return@handlePacket
    }

    val npc = neighboringNpcs.firstOrNull { it.index == this.packet.npcIndex } ?: return@handlePacket

    if (npc.entry == null) {
        logger.debug { "Invalid NPC clicked $npc" }
        return@handlePacket
    }

    with(player) {
        cancelAll()
        queueStrong {
            faceActor(npc::index)
            routeTo(npc) {
                angleTo(npc)
            }
        }
    }
}
