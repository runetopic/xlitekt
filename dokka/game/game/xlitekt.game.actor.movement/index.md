//[game](../../index.md)/[xlitekt.game.actor.movement](index.md)

# Package xlitekt.game.actor.movement

## Types

| Name | Summary |
|---|---|
| [Direction](-direction/index.md) | [jvm]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [Direction](-direction/index.md)(val packedDirection: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [Movement](-movement/index.md) | [jvm]<br>class [Movement](-movement/index.md) |
| [MovementRequest](-movement-request/index.md) | [jvm]<br>data class [MovementRequest](-movement-request/index.md)(val reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?, val waypoints: IntArrayList, val failed: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val alternative: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [MovementSpeed](-movement-speed/index.md) | [jvm]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [MovementSpeed](-movement-speed/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [MovementStep](-movement-step/index.md) | [jvm]<br>data class [MovementStep](-movement-step/index.md)(val speed: [MovementSpeed](-movement-speed/index.md), val location: [Location](../xlitekt.game.world.map/-location/index.md), val previousLocation: [Location](../xlitekt.game.world.map/-location/index.md), val direction: [Direction](-direction/index.md)) |
