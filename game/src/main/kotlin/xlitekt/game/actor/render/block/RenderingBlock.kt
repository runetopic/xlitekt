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
internal fun Collection<HighDefinitionRenderingBlock>.invokeHighDefinitionRenderingBlock(player: Player): ByteArray = buildPacket {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
    associateWith { it.renderingBlock.packet.invoke(it.render).readBytes() }.forEach {
        player.setLowDefinitionRenderingBlock(it.key, it.value)
        player.setAlternativeRenderingBlock(it.key.render, it.key.renderingBlock, it.value)
        writeBytes(it::value)
    }
}.readBytes()

/**
 * Creates a new ByteArray from a collection of LowDefinitionRenderingBlock.
 * This is used by the player info packet for building low definition blocks for players.
 */
internal fun Collection<LowDefinitionRenderingBlock>.invokeLowDefinitionRenderingBlock(): ByteArray = buildPacket {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
    map(LowDefinitionRenderingBlock::bytes).forEach { writeBytes { it } }
}.readBytes()

/**
 * Creates a new ByteArray from a collection of AlternativeDefinitionRenderingBlock.
 * This is used by the player update for both high and low definition updates that require outside player checks.
 */
internal fun Collection<AlternativeDefinitionRenderingBlock>.invokeAlternativeDefinitionRenderingBlock(): ByteArray = buildPacket {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
    map(AlternativeDefinitionRenderingBlock::bytes).forEach { writeBytes { it } }
}.readBytes()

fun BytePacketBuilder.buildNPCUpdateBlocks(npc: NPC) = with(npc.highDefinitionRenderingBlocks()) {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x4 else it })
    for (update in this) {
        writeBytes(update.renderingBlock.packet.invoke(update.render)::readBytes)
    }
}

private fun BytePacketBuilder.writeMask(mask: Int) {
    if (mask > 0xff) writeShortLittleEndian { mask } else writeByte { mask }
}
