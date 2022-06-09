package script.packet.assembler

import io.ktor.util.moveToByteArray
import org.jctools.maps.NonBlockingHashMapLong
import script.packet.assembler.NPCExtendedInfoPacketAssembler.ActivityUpdateType.Adding
import script.packet.assembler.NPCExtendedInfoPacketAssembler.ActivityUpdateType.Moving
import script.packet.assembler.NPCExtendedInfoPacketAssembler.ActivityUpdateType.Removing
import script.packet.assembler.NPCExtendedInfoPacketAssembler.ActivityUpdateType.Updating
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Viewport
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.withinDistance
import xlitekt.shared.buffer.BitAccess
import xlitekt.shared.buffer.allocateDynamic
import xlitekt.shared.buffer.withBitAccess
import xlitekt.shared.buffer.writeBytes
import java.nio.ByteBuffer
import java.util.Optional

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<NPCInfoPacket>(opcode = 90, size = -2) {
    allocateDynamic(25_000) {
        val blocks = ByteBuffer.allocate(5000)
        withBitAccess {
            writeBits(8, viewport.npcs.size)
            highDefinition(viewport, blocks, highDefinitionUpdates, movementStepsUpdates)
            lowDefinition(viewport, blocks, highDefinitionUpdates)
            if (blocks.position() > 0) {
                writeBits(15, Short.MAX_VALUE.toInt())
            }
        }
        blocks.limit(blocks.position())
        blocks.rewind()
        writeBytes(blocks.moveToByteArray())
    }
}

fun BitAccess.highDefinition(
    viewport: Viewport,
    blocks: ByteBuffer,
    highDefinitionUpdates: NonBlockingHashMapLong<Optional<ByteArray>>,
    movementStepsUpdates: NonBlockingHashMapLong<Optional<MovementStep>>
) {
    val playerLocation = viewport.player.location
    viewport.npcs.forEach {
        // Check the activities this npc is doing.
        val activity = highDefinitionActivities(it, playerLocation, highDefinitionUpdates[it.indexL], movementStepsUpdates[it.indexL])
        if (activity == null) {
            // This npc has no activity update (false).
            writeBit(false)
            return@forEach
        }
        // This npc has an activity update (true).
        writeBit(true)
        val updating = highDefinitionUpdates[it.indexL]?.isPresent == true
        activity.writeBits(this, it, updating, playerLocation, movementStepsUpdates[it.indexL] ?: Optional.empty())
        if (activity !is Removing) {
            if (updating) blocks.writeBytes(highDefinitionUpdates[it.indexL]!!.get())
        }
    }
    viewport.npcs.removeAll { !it.location.withinDistance(playerLocation) }
}

fun BitAccess.lowDefinition(viewport: Viewport, blocks: ByteBuffer, highDefinitionUpdates: NonBlockingHashMapLong<Optional<ByteArray>>) {
    val player = viewport.player
    player.zone().neighboringNpcs().forEach {
        val updates = highDefinitionUpdates[it.indexL]
        // Check the activities this npc is doing.
        val activity = lowDefinitionActivities(viewport, it, player.location, updates)
        if (activity != null) {
            val updating = updates?.isPresent == true
            // Write the corresponding activity bits depending on what the npc is doing.
            activity.writeBits(this, it, updating, playerLocation = player.location, step = Optional.empty())
            if (activity is Adding) {
                viewport.npcs += it
                if (updating) blocks.writeBytes(highDefinitionUpdates[it.indexL]!!.get())
            }
        }
    }
}

fun highDefinitionActivities(
    npc: NPC,
    playerLocation: Location,
    update: Optional<ByteArray>?,
    step: Optional<MovementStep>?
): ActivityUpdateType? {
    return when {
        // If the npc is not within normal distance of the player.
        !npc.location.withinDistance(playerLocation) -> Removing
        // If the npc is moving.
        step?.isPresent == true -> Moving
        // If the npc has a block update.
        update?.isPresent == true -> Updating
        else -> null
    }
}

fun lowDefinitionActivities(
    viewport: Viewport,
    npc: NPC?,
    playerLocation: Location,
    updates: Optional<ByteArray>?
): ActivityUpdateType? {
    return when {
        // If the npc is within our normal distance and the server has not added them to the player viewport.
        npc != null && npc !in viewport.npcs && npc.location.withinDistance(playerLocation) -> Adding
        updates?.isPresent == false -> null
        else -> null
    }
}

sealed class ActivityUpdateType {
    object Removing : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, npc: NPC, updating: Boolean, playerLocation: Location, step: Optional<MovementStep>) {
            bits.writeBits(2, 3)
        }
    }

    object Moving : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, npc: NPC, updating: Boolean, playerLocation: Location, step: Optional<MovementStep>) {
            bits.writeBits(2, 1) // walk only
            val movementStep = step.orElseThrow()
            val opcode = movementStep.direction.opcodeForNPCDirection
            bits.writeBits(3, opcode)
            bits.writeBit(updating)
        }
    }

    object Updating : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, npc: NPC, updating: Boolean, playerLocation: Location, step: Optional<MovementStep>) {
            bits.writeBits(2, 0)
        }
    }

    object Adding : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, npc: NPC, updating: Boolean, playerLocation: Location, step: Optional<MovementStep>) {
            bits.writeBits(15, npc.index)
            bits.writeBits(1, 0) // if 1 == 1 read 32 bits they just don't use it atm. Looks like they're working on something
            bits.writeBit(updating)
            bits.writeBits(3, 0) // TODO orientation
            bits.writeBits(8, (npc.location.z - playerLocation.z).let { if (it < 127) it + 256 else it })
            bits.writeBit(false) // TODO handle teleporting
            bits.writeBits(14, npc.id)
            bits.writeBits(8, (npc.location.x - playerLocation.x).let { if (it < 127) it + 256 else it })
        }
    }

    abstract fun writeBits(
        bits: BitAccess,
        npc: NPC,
        updating: Boolean,
        playerLocation: Location,
        step: Optional<MovementStep>
    )
}
