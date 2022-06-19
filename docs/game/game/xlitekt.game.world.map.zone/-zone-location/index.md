//[game](../../../index.md)/[xlitekt.game.world.map.zone](../index.md)/[ZoneLocation](index.md)

# ZoneLocation

[jvm]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)

value class [ZoneLocation](index.md)(val packedLocation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))

#### Author

Kris

Jordan Abraham

## Constructors

| | |
|---|---|
| [ZoneLocation](-zone-location.md) | [jvm]<br>fun [ZoneLocation](-zone-location.md)(x: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), z: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) |
| [ZoneLocation](-zone-location.md) | [jvm]<br>fun [ZoneLocation](-zone-location.md)(packedLocation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [toString](to-string.md) | [jvm]<br>open override fun [toString](to-string.md)(): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |

## Properties

| Name | Summary |
|---|---|
| [level](level.md) | [jvm]<br>val [level](level.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [location](location.md) | [jvm]<br>val [location](location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [packedLocation](packed-location.md) | [jvm]<br>val [packedLocation](packed-location.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [regionId](region-id.md) | [jvm]<br>val [regionId](region-id.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [regionX](region-x.md) | [jvm]<br>val [regionX](region-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [regionZ](region-z.md) | [jvm]<br>val [regionZ](region-z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [x](x.md) | [jvm]<br>val [x](x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [z](z.md) | [jvm]<br>val [z](z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Extensions

| Name | Summary |
|---|---|
| [transform](../transform.md) | [jvm]<br>fun [ZoneLocation](index.md).[transform](../transform.md)(deltaX: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), deltaZ: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), deltaLevel: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0): [ZoneLocation](index.md) |
