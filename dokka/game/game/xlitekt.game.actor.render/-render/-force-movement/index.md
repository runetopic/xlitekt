//[game](../../../../index.md)/[xlitekt.game.actor.render](../../index.md)/[Render](../index.md)/[ForceMovement](index.md)

# ForceMovement

[jvm]\
data class [ForceMovement](index.md)(val currentLocation: [Location](../../../xlitekt.game.world.map/-location/index.md), val firstLocation: [Location](../../../xlitekt.game.world.map/-location/index.md), val secondLocation: [Location](../../../xlitekt.game.world.map/-location/index.md)?, val firstDelay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val secondDelay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val rotation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) : [Render](../index.md)

## Constructors

| | |
|---|---|
| [ForceMovement](-force-movement.md) | [jvm]<br>fun [ForceMovement](-force-movement.md)(currentLocation: [Location](../../../xlitekt.game.world.map/-location/index.md), firstLocation: [Location](../../../xlitekt.game.world.map/-location/index.md), secondLocation: [Location](../../../xlitekt.game.world.map/-location/index.md)?, firstDelay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, secondDelay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, rotation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) |

## Functions

| Name | Summary |
|---|---|
| [hasAlternative](../has-alternative.md) | [jvm]<br>fun [hasAlternative](../has-alternative.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if a render has an alternative update. |

## Properties

| Name | Summary |
|---|---|
| [currentLocation](current-location.md) | [jvm]<br>val [currentLocation](current-location.md): [Location](../../../xlitekt.game.world.map/-location/index.md) |
| [firstDelay](first-delay.md) | [jvm]<br>val [firstDelay](first-delay.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [firstLocation](first-location.md) | [jvm]<br>val [firstLocation](first-location.md): [Location](../../../xlitekt.game.world.map/-location/index.md) |
| [rotation](rotation.md) | [jvm]<br>val [rotation](rotation.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [secondDelay](second-delay.md) | [jvm]<br>val [secondDelay](second-delay.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [secondLocation](second-location.md) | [jvm]<br>val [secondLocation](second-location.md): [Location](../../../xlitekt.game.world.map/-location/index.md)? |
