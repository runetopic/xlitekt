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
 * Creates a new ByteArray for the mask information of this players high definition rendering blocks.
 */
internal fun Collection<HighDefinitionRenderingBlock>.createHighDefinitionsMask(): ByteArray {
    return buildPacket {
        writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
    }.readBytes()
}

/**
 * Creates a new ByteArray for the mask information of this players low definition rendering blocks.
 */
internal fun Collection<LowDefinitionRenderingBlock>.createLowDefinitionsMask(): ByteArray {
    return buildPacket {
        writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
    }.readBytes()
}

/**
 * Creates a new ByteArray of this HighDefinitionRenderingBlock.
 * This also converts the HighDefinitionRenderingBlock into a LowDefinitionRenderingBlock and sets it to the player.
 * This also invokes an alternative rendering block if applicable to this players HighDefinitionRenderingBlock.
 *
 * Alternative rendering blocks are for rendering blocks that requires the outside player perspective.
 * An example of an alternative rendering block is for hit splat tinting as it requires the check of the outside player varbit.
 */
internal fun HighDefinitionRenderingBlock.invokeHighDefinitionRenderingBlock(player: Player): ByteArray {
    return buildPacket {
        val bytes = renderingBlock.packet.invoke(render).readBytes()
        if (render.hasAlternative()) {
            player.addLowDefinitionRenderingBlock(this@invokeHighDefinitionRenderingBlock, bytes.copyOfRange(0, bytes.size / 2))
            player.addAlternativeRenderingBlock(render, bytes.copyOfRange(bytes.size / 2, bytes.size))
        } else {
            player.addLowDefinitionRenderingBlock(this@invokeHighDefinitionRenderingBlock, bytes)
        }
        writeBytes { bytes }
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
