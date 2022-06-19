//[game](../../../index.md)/[xlitekt.game.actor.render.block](../index.md)/[LowDefinitionRenderingBlock](index.md)

# LowDefinitionRenderingBlock

[jvm]\
data class [LowDefinitionRenderingBlock](index.md)(val render: [Render](../../xlitekt.game.actor.render/-render/index.md), val renderingBlock: [RenderingBlock](../-rendering-block/index.md), val bytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html))

#### Author

Jordan Abraham

A LowDefinitionRenderingBlock is to represent a rendering block that is cached to this player. This is used by the player info packet for low definition updates.

## Constructors

| | |
|---|---|
| [LowDefinitionRenderingBlock](-low-definition-rendering-block.md) | [jvm]<br>fun [LowDefinitionRenderingBlock](-low-definition-rendering-block.md)(render: [Render](../../xlitekt.game.actor.render/-render/index.md), renderingBlock: [RenderingBlock](../-rendering-block/index.md), bytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Properties

| Name | Summary |
|---|---|
| [bytes](bytes.md) | [jvm]<br>val [bytes](bytes.md): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html) |
| [render](render.md) | [jvm]<br>val [render](render.md): [Render](../../xlitekt.game.actor.render/-render/index.md) |
| [renderingBlock](rendering-block.md) | [jvm]<br>val [renderingBlock](rendering-block.md): [RenderingBlock](../-rendering-block/index.md) |
