//[game](../../index.md)/[xlitekt.game.world.map](index.md)

# Package xlitekt.game.world.map

## Types

| Name | Summary |
|---|---|
| [CollisionMap](-collision-map/index.md) | [jvm]<br>object [CollisionMap](-collision-map/index.md) |
| [GameObject](-game-object/index.md) | [jvm]<br>data class [GameObject](-game-object/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val location: [Location](-location/index.md), val shape: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val rotation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [GameObjectShape](-game-object-shape/index.md) | [jvm]<br>object [GameObjectShape](-game-object-shape/index.md)<br>Kris |
| [Location](-location/index.md) | [jvm]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [Location](-location/index.md)(val packedLocation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [directionTo](direction-to.md) | [jvm]<br>fun [Location](-location/index.md).[directionTo](direction-to.md)(end: [Location](-location/index.md)): [Direction](../xlitekt.game.actor.movement/-direction/index.md) |
| [localX](local-x.md) | [jvm]<br>fun [Location](-location/index.md).[localX](local-x.md)(location: [Location](-location/index.md), size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 104): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [localZ](local-z.md) | [jvm]<br>fun [Location](-location/index.md).[localZ](local-z.md)(location: [Location](-location/index.md), size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 104): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [transform](transform.md) | [jvm]<br>fun [Location](-location/index.md).[transform](transform.md)(xOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), yOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), levelOffset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0): [Location](-location/index.md) |
| [withinDistance](within-distance.md) | [jvm]<br>fun [Location](-location/index.md).[withinDistance](within-distance.md)(other: [Location](-location/index.md), distance: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 15): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
