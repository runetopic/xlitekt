//[game](../../../index.md)/[xlitekt.game.world.map](../index.md)/[Location](index.md)

# Location

[jvm]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)

value class [Location](index.md)(val packedLocation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))

#### Author

Jordan Abraham

Tyler Telis

## Constructors

| | |
|---|---|
| [Location](-location.md) | [jvm]<br>fun [Location](-location.md)(x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), z: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) |
| [Location](-location.md) | [jvm]<br>fun [Location](-location.md)(packedLocation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | [jvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Properties

| Name | Summary |
|---|---|
| [level](level.md) | [jvm]<br>val [level](level.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [packedLocation](packed-location.md) | [jvm]<br>val [packedLocation](packed-location.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [regionId](region-id.md) | [jvm]<br>val [regionId](region-id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [regionLocation](region-location.md) | [jvm]<br>val [regionLocation](region-location.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [regionX](region-x.md) | [jvm]<br>val [regionX](region-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [regionZ](region-z.md) | [jvm]<br>val [regionZ](region-z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [x](x.md) | [jvm]<br>val [x](x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [z](z.md) | [jvm]<br>val [z](z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [zoneId](zone-id.md) | [jvm]<br>val [zoneId](zone-id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [zoneLocation](zone-location.md) | [jvm]<br>val [zoneLocation](zone-location.md): [ZoneLocation](../../xlitekt.game.world.map.zone/-zone-location/index.md) |
| [zoneX](zone-x.md) | [jvm]<br>val [zoneX](zone-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [zoneZ](zone-z.md) | [jvm]<br>val [zoneZ](zone-z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Extensions

| Name | Summary |
|---|---|
| [directionTo](../direction-to.md) | [jvm]<br>fun [Location](index.md).[directionTo](../direction-to.md)(end: [Location](index.md)): [Direction](../../xlitekt.game.actor.movement/-direction/index.md) |
| [localX](../local-x.md) | [jvm]<br>fun [Location](index.md).[localX](../local-x.md)(location: [Location](index.md), size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 104): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [localZ](../local-z.md) | [jvm]<br>fun [Location](index.md).[localZ](../local-z.md)(location: [Location](index.md), size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 104): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [transform](../transform.md) | [jvm]<br>fun [Location](index.md).[transform](../transform.md)(xOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), yOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), levelOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0): [Location](index.md) |
| [withinDistance](../within-distance.md) | [jvm]<br>fun [Location](index.md).[withinDistance](../within-distance.md)(other: [Location](index.md), distance: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 15): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
