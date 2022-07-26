package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeFully
import script.packet.assembler.NPCInfoPacketAssembler.ActivityUpdateType.Adding
import script.packet.assembler.NPCInfoPacketAssembler.ActivityUpdateType.Moving
import script.packet.assembler.NPCInfoPacketAssembler.ActivityUpdateType.Removing
import script.packet.assembler.NPCInfoPacketAssembler.ActivityUpdateType.Updating
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Viewport
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.withinDistance
import xlitekt.shared.buffer.BitAccess
import xlitekt.shared.buffer.dynamicBuffer
import xlitekt.shared.buffer.withBitAccess
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<NPCInfoPacket>(opcode = 78, size = -2) {
    it.writeBytes(
        dynamicBuffer {
            it.withBitAccess {
                writeBits(8, viewport.npcs.size)
                highDefinition(viewport, this@dynamicBuffer, highDefinitionUpdates, movementStepsUpdates)
                lowDefinition(viewport, this@dynamicBuffer, highDefinitionUpdates)
                if (isNotEmpty) {
                    writeBits(15, Short.MAX_VALUE.toInt())
                }
            }
        }
    )
}

fun BitAccess.highDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    highDefinitionUpdates: Array<ByteArray?>,
    movementStepsUpdates: Array<MovementStep?>
) {
    val playerLocation = viewport.player.location
    viewport.npcs.forEach {
        // Check the activities this npc is doing.
        val activity = highDefinitionActivities(it, playerLocation, highDefinitionUpdates[it.index], movementStepsUpdates[it.index])
        if (activity == null) {
            // This npc has no activity update (false).
            writeBit(false)
            return@forEach
        }
        // This npc has an activity update (true).
        writeBit(true)
        val updating = highDefinitionUpdates[it.index] != null
        activity.writeBits(this, it, updating, playerLocation, movementStepsUpdates[it.index])
        if (activity !is Removing) {
            if (updating) blocks.writeFully(highDefinitionUpdates[it.index]!!)
        }
    }
    viewport.npcs.removeAll { !it.location.withinDistance(playerLocation) }
}

fun BitAccess.lowDefinition(viewport: Viewport, blocks: BytePacketBuilder, highDefinitionUpdates: Array<ByteArray?>) {
    val player = viewport.player
    player.zone.neighboringNpcs().forEach {
        val updates = highDefinitionUpdates[it.index]
        // Check the activities this npc is doing.
        val activity = lowDefinitionActivities(viewport, it, player.location, updates)
        if (activity != null) {
            val updating = updates != null
            // Write the corresponding activity bits depending on what the npc is doing.
            activity.writeBits(this, it, updating, playerLocation = player.location, step = null)
            if (activity is Adding) {
                viewport.npcs += it
                if (updating) blocks.writeFully(highDefinitionUpdates[it.index]!!)
            }
        }
    }
}

fun highDefinitionActivities(
    npc: NPC,
    playerLocation: Location,
    update: ByteArray?,
    step: MovementStep?
): ActivityUpdateType? {
    return when {
        // If the npc is not within normal distance of the player.
        !npc.location.withinDistance(playerLocation) -> Removing
        // If the npc is moving.
        step != null -> Moving
        // If the npc has a block update.
        update != null -> Updating
        else -> null
    }
}

fun lowDefinitionActivities(
    viewport: Viewport,
    npc: NPC?,
    playerLocation: Location,
    updates: ByteArray?
): ActivityUpdateType? {
    return when {
        // If the npc is within our normal distance and the server has not added them to the player viewport.
        npc != null && npc !in viewport.npcs && npc.location.withinDistance(playerLocation) -> Adding
        updates == null -> null
        else -> null
    }
}

sealed class ActivityUpdateType {
    object Removing : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, npc: NPC, updating: Boolean, playerLocation: Location, step: MovementStep?) {
            bits.writeBits(2, 3)
        }
    }

    object Moving : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, npc: NPC, updating: Boolean, playerLocation: Location, step: MovementStep?) {
            bits.writeBits(2, 1) // walk only
            val opcode = step?.direction?.opcodeForNPCDirection ?: 0
            bits.writeBits(3, opcode)
            bits.writeBit(updating)
        }
    }

    object Updating : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, npc: NPC, updating: Boolean, playerLocation: Location, step: MovementStep?) {
            bits.writeBits(2, 0)
        }
    }

    object Adding : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, npc: NPC, updating: Boolean, playerLocation: Location, step: MovementStep?) {
            bits.writeBits(15, npc.index)
            bits.writeBits(1, 0) // if 1 == 1 read 32 bits they just don't use it atm. Looks like they're working on something
            bits.writeBit(updating)
            bits.writeBits(3, 0) // TODO orientation
            bits.writeBits(5, (npc.location.z - playerLocation.z).let { if (it < 15) it + 32 else it })
            bits.writeBit(false) // TODO handle teleporting
            bits.writeBits(14, npc.id)
            bits.writeBits(5, (npc.location.x - playerLocation.x).let { if (it < 15) it + 32 else it })
        }
    }

    abstract fun writeBits(
        bits: BitAccess,
        npc: NPC,
        updating: Boolean,
        playerLocation: Location,
        step: MovementStep?
    )
}
