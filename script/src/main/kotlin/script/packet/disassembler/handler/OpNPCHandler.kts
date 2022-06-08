package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.actor.*
import xlitekt.game.content.vars.VarPlayer
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

//    // Teleport movement (ctrl+click teleporting)
//    if (player.rights >= 2 && packet.isModified) {
//        player.teleportTo { waypoints.last }
//        return@onPacketHandler
//    }
//
//    // Toggles Actor's speed only for the duration of the movement (if isModified=true)
//    player.speed { (VarPlayer.ToggleRun in player.vars).let { if (packet.isModified) !it else it } }

    with(player) {
        cancelAll()
        routeTo(
            npc = npc,
            reachAction = {
                angleTo(npc)
            }
        )
    }
}
