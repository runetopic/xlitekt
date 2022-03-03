package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Client.Companion.world
import xlitekt.game.actor.player.Viewport
import xlitekt.game.actor.render.block.buildNPCUpdateBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.game.world.map.location.withinDistance
import xlitekt.shared.buffer.BitAccess
import xlitekt.shared.buffer.withBitAccess
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<NPCInfoPacket>(opcode = 78, size = -2) {
    buildPacket {
        val blocks = BytePacketBuilder()
        withBitAccess {
            viewport.let {
                writeBits(8, it.localNPCs.size)
                highDefinition(it, blocks)
                lowDefinition(it, blocks)
            }

            if (blocks.size > 0) {
                writeBits(15, Short.MAX_VALUE.toInt())
            }
        }
        writePacket(blocks.build())
    }
}

fun BitAccess.lowDefinition(viewport: Viewport, blocks: BytePacketBuilder) {
    world.npcs.toList().filterNotNull().forEach { // TODO iterate visible map regions to display all npcs when we do region support.
        if (viewport.localNPCs.contains(it)) return@forEach
        writeBits(15, it.index)
        writeBits(1, 0) // if 1 == 1 read 32 bits they just don't use it atm. Looks like they're working on something
        var x = it.location.x - viewport.player.location.x
        var z = it.location.z - viewport.player.location.z
        if (x < 15) x += 32
        if (z < 15) z += 32
        writeBits(1, it.hasPendingUpdate().toInt())
        writeBits(3, 512) // TODO orientation
        writeBits(5, z)
        writeBits(1, 0) // TODO handle teleporting
        writeBits(14, it.id)
        writeBits(5, x)
        viewport.localNPCs.add(it)
        if (it.hasPendingUpdate()) blocks.buildNPCUpdateBlocks(it)
    }
}

fun BitAccess.highDefinition(viewport: Viewport, blocks: BytePacketBuilder) {
    viewport.localNPCs.forEach {
        if (!it.location.withinDistance(viewport.player, 14)) {
            removeNPC()
            return@forEach
        }
        val updating = processHighDefinitionNPC(it)
        if (updating) blocks.buildNPCUpdateBlocks(it)
    }
}

fun BitAccess.processHighDefinitionNPC(npc: NPC): Boolean {
    // TODO Extract this out into an enum or something instead of passing around a bunch of booleans.
    val needsWalkUpdate = false
    val needsUpdate = npc.hasPendingUpdate()
    writeBits(1, needsUpdate.toInt())
    when {
        needsWalkUpdate -> {
            writeBits(2, 1) // TODO handle run direction
            writeBits(3, -1) // TODO handle walk direction
            // if run direction is not -1 send a bit of 3 with the next run direction
            writeBits(1, needsUpdate.toInt())
        }
        needsUpdate -> {
            writeBits(2, 0)
        }
    }
    return needsUpdate
}

fun BitAccess.removeNPC() {
    writeBits(1, 1)
    writeBits(2, 3)
}
