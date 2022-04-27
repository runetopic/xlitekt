package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeShortLittleEndian

data class RenderingBlock(
    val index: Int,
    val mask: Int,
    val packet: Render.() -> ByteReadPacket
)

fun Collection<Render>.buildPlayerUpdateBlocks(player: Player, cache: Boolean = true) = buildPacket {
    val blocks = this@buildPlayerUpdateBlocks.toSortedPlayerBlocks()
    writePlayerMasks(blocks.values)
    for (block in blocks) {
        // If (cache == true) then it will create a new block buffer and cache the new one to the player.
        val packet = if (cache) block.value.packet.invoke(block.key) else player.cachedUpdates()[block.key]
        if (packet != null) {
            if (cache) player.cacheUpdateBlock(block.key, packet)
            writeBytes(packet.copy()::readBytes)
        }
    }
}

fun BytePacketBuilder.buildNPCUpdateBlocks(npc: NPC) {
    val blocks = npc.pendingUpdates().toSortedNPCBlocks()
    writeNPCMasks(blocks.values)
    for (block in blocks) {
        writeBytes(block.value.packet.invoke(block.key)::readBytes)
    }
}

private fun BytePacketBuilder.writePlayerMasks(blocks: Collection<RenderingBlock>) = writeMask(
    blocks.fold(0) { current, next -> current or next.mask }.let { if (it > 0xff) it or 0x10 else it }
)

private fun BytePacketBuilder.writeNPCMasks(blocks: Collection<RenderingBlock>) = writeMask(
    blocks.fold(0) { current, next -> current or next.mask }.let { if (it > 0xff) it or 0x4 else it }
)

private fun BytePacketBuilder.writeMask(mask: Int) {
    if (mask > 0xff) writeShortLittleEndian { mask } else writeByte { mask }
}

private fun Collection<Render>.toSortedPlayerBlocks() = map { it to PlayerUpdateBlockListener.listeners[it::class]!! }.sortedBy { it.second.index }.toMap()
private fun Collection<Render>.toSortedNPCBlocks() = map { it to NPCUpdateBlockListener.listeners[it::class]!! }.sortedBy { it.second.index }.toMap()
