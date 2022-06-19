//[game](../../index.md)/[xlitekt.game.actor.render.block](index.md)/[invokeHighDefinitionPlayerRenderingBlocks](invoke-high-definition-player-rendering-blocks.md)

# invokeHighDefinitionPlayerRenderingBlocks

[jvm]\
fun [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[HighDefinitionRenderingBlock](-high-definition-rendering-block/index.md)&gt;.[invokeHighDefinitionPlayerRenderingBlocks](invoke-high-definition-player-rendering-blocks.md)(player: [Player](../xlitekt.game.actor.player/-player/index.md)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)

Creates a new ByteArray of from a collection of HighDefinitionRenderingBlock for players. This also converts the HighDefinitionRenderingBlock into a LowDefinitionRenderingBlock and sets it to the player. This also invokes an alternative rendering block if applicable to this players HighDefinitionRenderingBlock.

Alternative rendering blocks are for rendering blocks that requires the outside player perspective. An example of an alternative rendering block is for hit splat tinting as it requires the check of the outside player varbit.

RenderingBlock byte buffers can be built in one of two ways. If the RenderingBlock has a known fixed size of bytes to build with, then the block is built with a ByteBuffer with a fixed capacity equal to that of the size of the RenderingBlock. If the RenderingBlock is defined with a size of -1, then this means the block requires a dynamically growing buffer when building. This is done with BytePacketBuilder.
