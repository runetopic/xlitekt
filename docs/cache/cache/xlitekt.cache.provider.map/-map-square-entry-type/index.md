//[cache](../../../index.md)/[xlitekt.cache.provider.map](../index.md)/[MapSquareEntryType](index.md)

# MapSquareEntryType

[jvm]\
data class [MapSquareEntryType](index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val regionX: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = id shr 8, val regionZ: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = id and 0xff, var terrain: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MapSquareEntryType.MapSquareTerrainLocation](-map-square-terrain-location/index.md)?&gt;&gt;&gt; = Array(LEVELS) { Array(MAP_SIZE) { arrayOfNulls(MAP_SIZE) } }, val locs: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MapSquareEntryType.MapSquareLocLocation](-map-square-loc-location/index.md)&gt;&gt;&gt;&gt; = Array(LEVELS) {
        Array(MAP_SIZE) {
            Array(MAP_SIZE) {
                mutableListOf()
            }
        }
    }) : [EntryType](../../xlitekt.cache.provider/-entry-type/index.md)

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [MapSquareEntryType](-map-square-entry-type.md) | [jvm]<br>fun [MapSquareEntryType](-map-square-entry-type.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), regionX: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = id shr 8, regionZ: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = id and 0xff, terrain: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MapSquareEntryType.MapSquareTerrainLocation](-map-square-terrain-location/index.md)?&gt;&gt;&gt; = Array(LEVELS) { Array(MAP_SIZE) { arrayOfNulls(MAP_SIZE) } }, locs: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MapSquareEntryType.MapSquareLocLocation](-map-square-loc-location/index.md)&gt;&gt;&gt;&gt; = Array(LEVELS) {         Array(MAP_SIZE) {             Array(MAP_SIZE) {                 mutableListOf()             }         }     }) |

## Types

| Name | Summary |
|---|---|
| [MapSquareLocLocation](-map-square-loc-location/index.md) | [jvm]<br>data class [MapSquareLocLocation](-map-square-loc-location/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val z: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val shape: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val rotation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [MapSquareTerrainLocation](-map-square-terrain-location/index.md) | [jvm]<br>data class [MapSquareTerrainLocation](-map-square-terrain-location/index.md)(val height: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val overlayId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val overlayPath: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val overlayRotation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val collision: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val underlayId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Properties

| Name | Summary |
|---|---|
| [id](id.md) | [jvm]<br>open override val [id](id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [locs](locs.md) | [jvm]<br>val [locs](locs.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[MapSquareEntryType.MapSquareLocLocation](-map-square-loc-location/index.md)&gt;&gt;&gt;&gt; |
| [regionX](region-x.md) | [jvm]<br>val [regionX](region-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [regionZ](region-z.md) | [jvm]<br>val [regionZ](region-z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [terrain](terrain.md) | [jvm]<br>var [terrain](terrain.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MapSquareEntryType.MapSquareTerrainLocation](-map-square-terrain-location/index.md)?&gt;&gt;&gt; |
