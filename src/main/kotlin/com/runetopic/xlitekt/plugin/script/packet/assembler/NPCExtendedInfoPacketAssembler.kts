package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.player.Viewport
import com.runetopic.xlitekt.game.actor.render.block.buildNPCUpdateBlocks
import com.runetopic.xlitekt.game.world.map.location.withinDistance
import com.runetopic.xlitekt.network.client.Client.Companion.world
import com.runetopic.xlitekt.network.packet.NPCInfoExtendedPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.BitAccess
import com.runetopic.xlitekt.shared.buffer.withBitAccess
import com.runetopic.xlitekt.shared.toInt
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<NPCInfoExtendedPacket>(opcode = 90, size = -2) {
    buildPacket {
        val blocks = BytePacketBuilder()
        withBitAccess {
            viewport.also {
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
        if (x < 127) x += 256
        if (z < 127) z += 256
        writeBits(1, it.hasPendingUpdate().toInt())
        writeBits(3, 512) // TODO orientation
        writeBits(8, z)
        writeBits(1, 0) // TODO handle teleporting
        writeBits(14, it.id)
        writeBits(8, x)
        viewport.localNPCs.add(it)
        if (it.hasPendingUpdate()) blocks.buildNPCUpdateBlocks(it)
    }
}

fun BitAccess.highDefinition(viewport: Viewport, blocks: BytePacketBuilder) {
    viewport.localNPCs.forEach {
        if (!it.location.withinDistance(viewport.player, 126)) {
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
