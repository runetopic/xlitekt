//[cache](../../index.md)/[xlitekt.cache.provider.map](index.md)

# Package xlitekt.cache.provider.map

## Types

| Name | Summary |
|---|---|
| [MapSquareEntryType](-map-square-entry-type/index.md) | [jvm]<br>data class [MapSquareEntryType](-map-square-entry-type/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val regionX: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = id shr 8, val regionZ: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = id and 0xff, var terrain: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MapSquareEntryType.MapSquareTerrainLocation](-map-square-entry-type/-map-square-terrain-location/index.md)?&gt;&gt;&gt; = Array(LEVELS) { Array(MAP_SIZE) { arrayOfNulls(MAP_SIZE) } }, val locs: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MapSquareEntryType.MapSquareLocLocation](-map-square-entry-type/-map-square-loc-location/index.md)&gt;&gt;&gt;&gt; = Array(LEVELS) {         Array(MAP_SIZE) {             Array(MAP_SIZE) {                 mutableListOf()             }         }     }) : [EntryType](../xlitekt.cache.provider/-entry-type/index.md) |
| [MapSquareEntryTypeProvider](-map-square-entry-type-provider/index.md) | [jvm]<br>class [MapSquareEntryTypeProvider](-map-square-entry-type-provider/index.md) : [EntryTypeProvider](../xlitekt.cache.provider/-entry-type-provider/index.md)&lt;[MapSquareEntryType](-map-square-entry-type/index.md)&gt; |
