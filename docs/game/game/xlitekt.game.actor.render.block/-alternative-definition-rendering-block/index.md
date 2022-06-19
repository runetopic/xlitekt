//[game](../../../index.md)/[xlitekt.game.actor.render.block](../index.md)/[AlternativeDefinitionRenderingBlock](index.md)

# AlternativeDefinitionRenderingBlock

[jvm]\
data class [AlternativeDefinitionRenderingBlock](index.md)(val render: [Render](../../xlitekt.game.actor.render/-render/index.md), val renderingBlock: [RenderingBlock](../-rendering-block/index.md), val bytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html))

#### Author

Jordan Abraham

A AlternativeDefinitionRenderingBlock represents a rendering block that requires an outside player perspective to use. An example of such a block is for hits which requires checking the outside player varbit before writing the block to the client. This is used by the player info packet for high and low definition updates.

## Constructors

| | |
|---|---|
| [AlternativeDefinitionRenderingBlock](-alternative-definition-rendering-block.md) | [jvm]<br>fun [AlternativeDefinitionRenderingBlock](-alternative-definition-rendering-block.md)(render: [Render](../../xlitekt.game.actor.render/-render/index.md), renderingBlock: [RenderingBlock](../-rendering-block/index.md), bytes: [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)) |

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
