package xlitekt.game.actor.render.block

import xlitekt.game.actor.render.Render

/**
 * @author Jordan Abraham
 *
 * A AlternativeDefinitionRenderingBlock represents a rendering block that requires an outside player perspective to use.
 * An example of such a block is for hits which requires checking the outside player varbit before writing the block to the client.
 * This is used by the player info packet for high and low definition updates.
 */
data class AlternativeDefinitionRenderingBlock(
    val render: Render,
    val renderingBlock: RenderingBlock,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LowDefinitionRenderingBlock

        if (renderingBlock != other.renderingBlock) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = renderingBlock.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
