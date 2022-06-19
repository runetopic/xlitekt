//[game](../../../index.md)/[xlitekt.game.actor.render.block](../index.md)/[RenderingBlock](index.md)

# RenderingBlock

[jvm]\
data class [RenderingBlock](index.md)(val index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val mask: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val fixed: [Render](../../xlitekt.game.actor.render/-render/index.md).([ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null, val dynamic: [Render](../../xlitekt.game.actor.render/-render/index.md).(BytePacketBuilder) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null, val indexL: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = index.toLong())

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [RenderingBlock](-rendering-block.md) | [jvm]<br>fun [RenderingBlock](-rendering-block.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), mask: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), fixed: [Render](../../xlitekt.game.actor.render/-render/index.md).([ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null, dynamic: [Render](../../xlitekt.game.actor.render/-render/index.md).(BytePacketBuilder) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null, indexL: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) = index.toLong()) |

## Properties

| Name | Summary |
|---|---|
| [dynamic](dynamic.md) | [jvm]<br>val [dynamic](dynamic.md): [Render](../../xlitekt.game.actor.render/-render/index.md).(BytePacketBuilder) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null |
| [fixed](fixed.md) | [jvm]<br>val [fixed](fixed.md): [Render](../../xlitekt.game.actor.render/-render/index.md).([ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? = null |
| [index](--index--.md) | [jvm]<br>val [index](--index--.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [indexL](index-l.md) | [jvm]<br>val [indexL](index-l.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [mask](mask.md) | [jvm]<br>val [mask](mask.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [size](size.md) | [jvm]<br>val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
