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

/**
 * @author Jordan Abraham
 */
data class RenderingBlock(
    val index: Int,
    val mask: Int,
    val packet: Render.() -> ByteReadPacket
)

/**
 * Creates a new ByteArray of from a collection of HighDefinitionRenderingBlock.
 * This also converts the HighDefinitionRenderingBlock into a LowDefinitionRenderingBlock and sets it to the player.
 * This also invokes an alternative rendering block if applicable to this players HighDefinitionRenderingBlock.
 *
 * Alternative rendering blocks are for rendering blocks that requires the outside player perspective.
 * An example of an alternative rendering block is for hit splat tinting as it requires the check of the outside player varbit.
 */
internal fun Collection<HighDefinitionRenderingBlock>.invokeHighDefinitionRenderingBlock(player: Player): ByteArray? {
    if (isEmpty()) return null
    return buildPacket {
        writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
        for (block in this@invokeHighDefinitionRenderingBlock) {
            val bytes = block.renderingBlock.packet.invoke(block.render).readBytes()
            if (block.render.hasAlternative()) {
                player.addLowDefinitionRenderingBlock(block, bytes.copyOfRange(0, bytes.size / 2))
                player.addAlternativeRenderingBlock(block.render, block.renderingBlock, bytes.copyOfRange(bytes.size / 2, bytes.size))
            } else {
                player.addLowDefinitionRenderingBlock(block, bytes)
                player.addAlternativeRenderingBlock(block.render, block.renderingBlock, bytes)
            }
            writeBytes { bytes }
        }
    }.readBytes()
}

/**
 * Creates a new ByteArray from a collection of LowDefinitionRenderingBlock.
 * This is used by the player info packet for building low definition blocks for players.
 */
internal fun Collection<LowDefinitionRenderingBlock>.invokeLowDefinitionRenderingBlock(): ByteArray? {
    if (isEmpty()) return null
    return buildPacket {
        writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
        for (block in this@invokeLowDefinitionRenderingBlock) {
            writeBytes(block::bytes)
        }
    }.readBytes()
}

/**
 * Creates a new ByteArray from a collection of AlternativeDefinitionRenderingBlock.
 * This is used by the player update for both high and low definition updates that require outside player checks.
 */
internal fun Collection<AlternativeDefinitionRenderingBlock>.invokeAlternativeDefinitionRenderingBlock(): ByteArray? {
    if (isEmpty()) return null
    return buildPacket {
        writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
        for (block in this@invokeAlternativeDefinitionRenderingBlock) {
            writeBytes(block::bytes)
        }
    }.readBytes()
}

fun BytePacketBuilder.buildNPCUpdateBlocks(npc: NPC) = with(npc.highDefinitionRenderingBlocks()) {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x4 else it })
    for (update in this) {
        writeBytes(update.renderingBlock.packet.invoke(update.render)::readBytes)
    }
}

private fun BytePacketBuilder.writeMask(mask: Int) {
    if (mask > 0xff) writeShortLittleEndian { mask } else writeByte { mask }
}
