//[game](../../index.md)/[xlitekt.game.actor.render.block.body](index.md)

# Package xlitekt.game.actor.render.block.body

## Types

| Name | Summary |
|---|---|
| [BodyPart](-body-part/index.md) | [jvm]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [BodyPart](-body-part/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [BodyPartBlock](-body-part-block/index.md) | [jvm]<br>data class [BodyPartBlock](-body-part-block/index.md)(val bodyPart: [BodyPart](-body-part/index.md)?, val builder: [BodyPartBuilder](-body-part-builder/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [BodyPartBlockListener](-body-part-block-listener/index.md) | [jvm]<br>class [BodyPartBlockListener](-body-part-block-listener/index.md) : [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), [BodyPartBlock](-body-part-block/index.md)&gt; |
| [BodyPartBuilder](-body-part-builder/index.md) | [jvm]<br>class [BodyPartBuilder](-body-part-builder/index.md)(val kit: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val gender: [Render.Appearance.Gender](../xlitekt.game.actor.render/-render/-appearance/-gender/index.md), val equipment: [Equipment](../xlitekt.game.content.container.equipment/-equipment/index.md)) |
| [BodyPartColor](-body-part-color/index.md) | [jvm]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [BodyPartColor](-body-part-color/index.md)(packedBodyPartColor: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
