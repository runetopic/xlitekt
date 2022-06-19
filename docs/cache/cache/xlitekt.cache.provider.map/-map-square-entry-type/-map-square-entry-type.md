//[cache](../../../index.md)/[xlitekt.cache.provider.map](../index.md)/[MapSquareEntryType](index.md)/[MapSquareEntryType](-map-square-entry-type.md)

# MapSquareEntryType

[jvm]\
fun [MapSquareEntryType](-map-square-entry-type.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), regionX: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = id shr 8, regionZ: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = id and 0xff, terrain: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MapSquareEntryType.MapSquareTerrainLocation](-map-square-terrain-location/index.md)?&gt;&gt;&gt; = Array(LEVELS) { Array(MAP_SIZE) { arrayOfNulls(MAP_SIZE) } }, locs: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MapSquareEntryType.MapSquareLocLocation](-map-square-loc-location/index.md)&gt;&gt;&gt;&gt; = Array(LEVELS) {
        Array(MAP_SIZE) {
            Array(MAP_SIZE) {
                mutableListOf()
            }
        }
    })
