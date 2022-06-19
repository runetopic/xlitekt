//[game](../../../index.md)/[xlitekt.game.actor.render](../index.md)/[Render](index.md)

# Render

[jvm]\
sealed class [Render](index.md)

#### Author

Jordan Abraham

Tyler Telis

## Types

| Name | Summary |
|---|---|
| [Appearance](-appearance/index.md) | [jvm]<br>class [Appearance](-appearance/index.md) : [Render](index.md) |
| [FaceActor](-face-actor/index.md) | [jvm]<br>data class [FaceActor](-face-actor/index.md)(val index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [FaceAngle](-face-angle/index.md) | [jvm]<br>data class [FaceAngle](-face-angle/index.md)(val angle: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [FaceLocation](-face-location/index.md) | [jvm]<br>data class [FaceLocation](-face-location/index.md)(val location: [Location](../../xlitekt.game.world.map/-location/index.md)) : [Render](index.md) |
| [ForceMovement](-force-movement/index.md) | [jvm]<br>data class [ForceMovement](-force-movement/index.md)(val currentLocation: [Location](../../xlitekt.game.world.map/-location/index.md), val firstLocation: [Location](../../xlitekt.game.world.map/-location/index.md), val secondLocation: [Location](../../xlitekt.game.world.map/-location/index.md)?, val firstDelay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val secondDelay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, val rotation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0) : [Render](index.md) |
| [Hit](-hit/index.md) | [jvm]<br>data class [Hit](-hit/index.md)(val actor: [Actor](../../xlitekt.game.actor/-actor/index.md), val splats: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitSplat](../-hit-splat/index.md)&gt;, val bars: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[HitBar](../-hit-bar/index.md)&gt;, val tint: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [Render](index.md) |
| [MovementType](-movement-type/index.md) | [jvm]<br>data class [MovementType](-movement-type/index.md)(val running: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [Render](index.md) |
| [NPCCustomLevel](-n-p-c-custom-level/index.md) | [jvm]<br>data class [NPCCustomLevel](-n-p-c-custom-level/index.md)(val level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [NPCTransmogrification](-n-p-c-transmogrification/index.md) | [jvm]<br>data class [NPCTransmogrification](-n-p-c-transmogrification/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [OverheadChat](-overhead-chat/index.md) | [jvm]<br>data class [OverheadChat](-overhead-chat/index.md)(val text: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [Render](index.md) |
| [PublicChat](-public-chat/index.md) | [jvm]<br>data class [PublicChat](-public-chat/index.md)(val message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val packedEffects: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val rights: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [Recolor](-recolor/index.md) | [jvm]<br>data class [Recolor](-recolor/index.md)(val hue: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val saturation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val luminance: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val startDelay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val endDelay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [Sequence](-sequence/index.md) | [jvm]<br>data class [Sequence](-sequence/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val delay: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [SpotAnimation](-spot-animation/index.md) | [jvm]<br>data class [SpotAnimation](-spot-animation/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val speed: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val height: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val rotation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [TemporaryMovementType](-temporary-movement-type/index.md) | [jvm]<br>data class [TemporaryMovementType](-temporary-movement-type/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [Render](index.md) |
| [UsernameOverride](-username-override/index.md) | [jvm]<br>data class [UsernameOverride](-username-override/index.md)(val prefix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val infix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val suffix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) : [Render](index.md) |

## Functions

| Name | Summary |
|---|---|
| [hasAlternative](has-alternative.md) | [jvm]<br>fun [hasAlternative](has-alternative.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if a render has an alternative update. |

## Inheritors

| Name |
|---|
| [Sequence](-sequence/index.md) |
| [UsernameOverride](-username-override/index.md) |
| [SpotAnimation](-spot-animation/index.md) |
| [PublicChat](-public-chat/index.md) |
| [FaceActor](-face-actor/index.md) |
| [FaceAngle](-face-angle/index.md) |
| [NPCTransmogrification](-n-p-c-transmogrification/index.md) |
| [Recolor](-recolor/index.md) |
| [ForceMovement](-force-movement/index.md) |
| [OverheadChat](-overhead-chat/index.md) |
| [FaceLocation](-face-location/index.md) |
| [NPCCustomLevel](-n-p-c-custom-level/index.md) |
| [Hit](-hit/index.md) |
| [MovementType](-movement-type/index.md) |
| [TemporaryMovementType](-temporary-movement-type/index.md) |
| [Appearance](-appearance/index.md) |
