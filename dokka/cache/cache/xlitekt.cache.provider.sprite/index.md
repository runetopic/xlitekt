//[cache](../../index.md)/[xlitekt.cache.provider.sprite](index.md)

# Package xlitekt.cache.provider.sprite

## Types

| Name | Summary |
|---|---|
| [Sprite](-sprite/index.md) | [jvm]<br>data class [Sprite](-sprite/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val width: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val height: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val pixels: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)) |
| [SpriteEntryType](-sprite-entry-type/index.md) | [jvm]<br>data class [SpriteEntryType](-sprite-entry-type/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), var name: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)? = null, var offsetsX: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)? = null, var offsetsY: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)? = null, var widths: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)? = null, var heights: [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html)? = null, var sprites: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Sprite](-sprite/index.md)&gt; = emptyList()) : [EntryType](../xlitekt.cache.provider/-entry-type/index.md) |
| [SpriteEntryTypeProvider](-sprite-entry-type-provider/index.md) | [jvm]<br>class [SpriteEntryTypeProvider](-sprite-entry-type-provider/index.md) : [EntryTypeProvider](../xlitekt.cache.provider/-entry-type-provider/index.md)&lt;[SpriteEntryType](-sprite-entry-type/index.md)&gt; |
