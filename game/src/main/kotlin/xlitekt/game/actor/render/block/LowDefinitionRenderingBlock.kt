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
    val mask: Int,
    val block: ByteArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as LowDefinitionRenderingBlock

        if (render != other.render) return false
        if (!block.contentEquals(other.block)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = render.hashCode()
        result = 31 * result + block.contentHashCode()
        return result
    }
}
