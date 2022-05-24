package xlitekt.game.actor.render.block

import xlitekt.game.actor.render.Render

/**
 * @author Jordan Abraham
 *
 * A HighDefinitionRenderingBlock represents a rendering block that this player currently needs processing for.
 * Once a HighDefinitionRenderingBlock is used, it is then converted into a LowDefinitionRenderingBlock.
 * This is used by the player info packet for high definition updates.
 */
internal data class HighDefinitionRenderingBlock(
    val render: Render,
    val renderingBlock: RenderingBlock
)
