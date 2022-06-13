package xlitekt.game.actor.render.block

import io.ktor.util.moveToByteArray
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.shared.buffer.dynamicBuffer
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
data class RenderingBlock(
    val index: Int,
    val mask: Int,
    val size: Int,
    val fixed: (Render.(ByteBuffer) -> Unit)? = null,
    val dynamic: (Render.(BytePacketBuilder) -> Unit)? = null,
    val indexL: Long = index.toLong()
)

/**
 * Creates a new ByteArray of from a collection of HighDefinitionRenderingBlock for players.
 * This also converts the HighDefinitionRenderingBlock into a LowDefinitionRenderingBlock and sets it to the player.
 * This also invokes an alternative rendering block if applicable to this players HighDefinitionRenderingBlock.
 *
 * Alternative rendering blocks are for rendering blocks that requires the outside player perspective.
 * An example of an alternative rendering block is for hit splat tinting as it requires the check of the outside player varbit.
 *
 * RenderingBlock byte buffers can be built in one of two ways. If the RenderingBlock has a known fixed size of bytes to build with,
 * then the block is built with a ByteBuffer with a fixed capacity equal to that of the size of the RenderingBlock. If the RenderingBlock
 * is defined with a size of -1, then this means the block requires a dynamically growing buffer when building. This is done with BytePacketBuilder.
 */
internal fun Collection<HighDefinitionRenderingBlock>.invokeHighDefinitionPlayerRenderingBlocks(player: Player) = dynamicBuffer {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
    associateWith { block ->
        val renderingBlock = block.renderingBlock
        val size = renderingBlock.size
        if (size == -1) {
            // If the rendering block has a size of -1, this means the block requires a buffer that can dynamically grow.
            dynamicBuffer { renderingBlock.dynamic?.invoke(block.render, this) }
        } else {
            // If the rendering block has a fixed size then we can just allocate with ByteBuffer with a fixed capacity.
            ByteBuffer.allocate(size).also {
                renderingBlock.fixed?.invoke(block.render, it)
            }.flip().moveToByteArray()
        }
    }.forEach {
        player.setLowDefinitionRenderingBlock(it.key, it.value)
        player.setAlternativeRenderingBlock(it.key.render, it.key.renderingBlock, it.value)
        for (b in it.value) {
            writeByte(b)
        }
    }
}

/**
 * Creates a new ByteArray from a collection of LowDefinitionRenderingBlock for players.
 * This is used by the player info packet for building low definition blocks for players.
 */
internal fun Collection<LowDefinitionRenderingBlock>.invokeLowDefinitionPlayerRenderingBlocks() = dynamicBuffer {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
    map(LowDefinitionRenderingBlock::bytes).forEach {
        for (b in it) {
            writeByte(b)
        }
    }
}

/**
 * Creates a new ByteArray from a collection of AlternativeDefinitionRenderingBlock for players.
 * This is used by the player update for both high and low definition updates that require outside player checks.
 */
internal fun Collection<AlternativeDefinitionRenderingBlock>.invokeAlternativeDefinitionPlayerRenderingBlocks() = dynamicBuffer {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
    map(AlternativeDefinitionRenderingBlock::bytes).forEach {
        for (b in it) {
            writeByte(b)
        }
    }
}

/**
 * Creates a new ByteArray from a collection of HighDefinitionRenderingBlock for npcs.
 * This is used by the npc update.
 */
internal fun Collection<HighDefinitionRenderingBlock>.invokeHighDefinitionNPCRenderingBlocks() = dynamicBuffer {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x4 else it })
    map { block ->
        val renderingBlock = block.renderingBlock
        val size = renderingBlock.size
        if (size == -1) {
            // If the rendering block has a size of -1, this means the block requires a buffer that can dynamically grow.
            dynamicBuffer { renderingBlock.dynamic?.invoke(block.render, this) }
        } else {
            // If the rendering block has a fixed size then we can just allocate with ByteBuffer with a fixed capacity.
            ByteBuffer.allocate(size).also {
                renderingBlock.fixed?.invoke(block.render, it)
            }.flip().moveToByteArray()
        }
    }.forEach {
        for (b in it) {
            writeByte(b)
        }
    }
}

private fun BytePacketBuilder.writeMask(mask: Int) {
    if (mask > 0xff) writeShortLittleEndian(mask.toShort()) else writeByte(mask.toByte())
}
