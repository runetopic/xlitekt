package xlitekt.game.actor.render.block

import xlitekt.game.actor.render.Render

/**
 * @author Jordan Abraham
 *
 * A LowDefinitionRenderingBlock is to represent a rendering block that is cached to this player.
 * This is used by the player info packet for low definition updates.
 */
data class LowDefinitionRenderingBlock(
    val render: Render,
    val renderingBlock: RenderingBlock,
    val bytes: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LowDefinitionRenderingBlock

        if (render != other.render) return false
        if (renderingBlock != other.renderingBlock) return false
        if (!bytes.contentEquals(other.bytes)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = render.hashCode()
        result = 31 * result + renderingBlock.hashCode()
        result = 31 * result + bytes.contentHashCode()
        return result
    }
}
