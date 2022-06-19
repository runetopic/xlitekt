//[game](../../../index.md)/[xlitekt.game.actor.render.block](../index.md)/[HighDefinitionRenderingBlock](index.md)

# HighDefinitionRenderingBlock

[jvm]\
data class [HighDefinitionRenderingBlock](index.md)(val render: [Render](../../xlitekt.game.actor.render/-render/index.md), val renderingBlock: [RenderingBlock](../-rendering-block/index.md))

#### Author

Jordan Abraham

A HighDefinitionRenderingBlock represents a rendering block that this player currently needs processing for. Once a HighDefinitionRenderingBlock is used, it is then converted into a LowDefinitionRenderingBlock. This is used by the player info packet for high definition updates.

## Constructors

| | |
|---|---|
| [HighDefinitionRenderingBlock](-high-definition-rendering-block.md) | [jvm]<br>fun [HighDefinitionRenderingBlock](-high-definition-rendering-block.md)(render: [Render](../../xlitekt.game.actor.render/-render/index.md), renderingBlock: [RenderingBlock](../-rendering-block/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [render](render.md) | [jvm]<br>val [render](render.md): [Render](../../xlitekt.game.actor.render/-render/index.md) |
| [renderingBlock](rendering-block.md) | [jvm]<br>val [renderingBlock](rendering-block.md): [RenderingBlock](../-rendering-block/index.md) |
