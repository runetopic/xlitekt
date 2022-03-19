package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.actor.render.block.buildNPCUpdateBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.withinDistance
import xlitekt.game.world.map.zone.Zones
import xlitekt.shared.buffer.BitAccess
import xlitekt.shared.buffer.withBitAccess

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<NPCInfoPacket>(opcode = 78, size = -2) {
    buildPacket {
        val blocks = BytePacketBuilder()
        withBitAccess {
            viewport.let {
                writeBits(8, it.npcs.size)
                highDefinition(it, blocks, playerLocations)
                lowDefinition(it, blocks, playerLocations)
            }

            if (blocks.size > 0) {
                writeBits(15, Short.MAX_VALUE.toInt())
            }
        }
        writePacket(blocks.build())
    }
}

fun BitAccess.lowDefinition(viewport: Viewport, blocks: BytePacketBuilder, playerLocations: Map<Player, Location>) {
    val playerLocation = playerLocations[viewport.player] ?: viewport.player.location

    val currentZoneLocation = playerLocation.toZoneLocation()

    val zones = buildSet {
        (-2..2).forEach { x ->
            (-2..2).forEach { z ->
                add(currentZoneLocation.transform(x, z))
            }
        }
    }.map { Zones[it.toFullLocation()] }.filter { it?.npcs?.isNotEmpty() == true }

    zones.forEach { zone ->
        if (zone == null) return@forEach

        zone.npcs
            .filter { !viewport.npcs.contains(it) }
            .filter { it.location.withinDistance(playerLocation, 14) }
            .forEach {
                writeBits(15, it.index)
                writeBits(1, 0) // if 1 == 1 read 32 bits they just don't use it atm. Looks like they're working on something
                var x = it.location.x - playerLocation.x
                var z = it.location.z - playerLocation.z
                if (x < 15) x += 32
                if (z < 15) z += 32
                writeBits(1, 0)
                writeBits(3, 0) // TODO orientation
                writeBits(5, z)
                writeBits(1, 0) // TODO handle teleporting
                writeBits(14, it.id)
                writeBits(5, x)
                viewport.npcs.add(it)
                // if (it.hasPendingUpdate()) blocks.buildNPCUpdateBlocks(it)
            }
    }
}

fun BitAccess.highDefinition(viewport: Viewport, blocks: BytePacketBuilder, playerLocations: Map<Player, Location>) {
    val location = playerLocations[viewport.player] ?: viewport.player.location

    viewport.npcs.forEach {
        if (!it.location.withinDistance(location, 14)) {
            removeNPC()
            // viewport.localNPCs.remove(it)
            return@forEach
        }
        val updating = processHighDefinitionNPC(it)
        if (updating) blocks.buildNPCUpdateBlocks(it)
    }
    viewport.npcs.removeAll { !it.location.withinDistance(location, 14) }
}

fun BitAccess.processHighDefinitionNPC(npc: NPC): Boolean {
    // TODO Extract this out into an enum or something instead of passing around a bunch of booleans.
    val needsWalkUpdate = false
    val needsUpdate = false
    writeBits(1, 0)
//    when {
//        needsWalkUpdate -> {
//            writeBits(2, 1) // TODO handle run direction
//            writeBits(3, -1) // TODO handle walk direction
//            // if run direction is not -1 send a bit of 3 with the next run direction
//            writeBits(1, needsUpdate.toInt())
//        }
//        needsUpdate -> {
//            writeBits(2, 0)
//        }
//    }
    return needsUpdate
}

fun BitAccess.removeNPC() {
    writeBits(1, 1)
    writeBits(2, 3)
}
