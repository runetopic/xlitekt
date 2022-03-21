package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.shared.buffer.writeBytes

data class RenderingBlock(
    val index: Int,
    val mask: Int,
    val packet: Render.() -> ByteReadPacket
)

fun Collection<Render>.buildPlayerUpdateBlocks(player: Player, cache: Boolean = true) = buildPacket {
    val blocks = this@buildPlayerUpdateBlocks.toSortedPlayerBlocks()
    writePlayerMasks(blocks.values)
    blocks.forEach {
        // If (cache == true) then it will create a new block buffer and cache the new one to the player.
        val packet = if (cache) it.value.packet.invoke(it.key) else player.cachedUpdates()[it.key]
        if (packet != null) {
            if (cache) player.cacheUpdateBlock(it.key, packet)
            writeBytes(packet.copy().readBytes())
        }
    }
}

fun BytePacketBuilder.buildNPCUpdateBlocks(npc: NPC) {
    val blocks = npc.pendingUpdates().toSortedNPCBlocks()
    writeNPCMasks(blocks.values)
    blocks.forEach {
        writeBytes(it.value.packet.invoke(it.key).readBytes())
    }
}

private fun BytePacketBuilder.writePlayerMasks(blocks: Collection<RenderingBlock>) = writeMask(
    blocks.fold(0) { current, next -> current or next.mask }.let { if (it > 0xff) it or 0x10 else it }
)

private fun BytePacketBuilder.writeNPCMasks(blocks: Collection<RenderingBlock>) = writeMask(
    blocks.fold(0) { current, next -> current or next.mask }.let { if (it > 0xff) it or 0x4 else it }
)

private fun BytePacketBuilder.writeMask(mask: Int) = if (mask > 0xff) writeShortLittleEndian(mask.toShort()) else writeByte(mask.toByte())

private fun Collection<Render>.toSortedPlayerBlocks(): Map<Render, RenderingBlock> = map { it to PlayerUpdateBlockListener.listeners[it::class]!! }.sortedBy { it.second.index }.toMap()
private fun Collection<Render>.toSortedNPCBlocks(): Map<Render, RenderingBlock> = map { it to NPCUpdateBlockListener.listeners[it::class]!! }.sortedBy { it.second.index }.toMap()
