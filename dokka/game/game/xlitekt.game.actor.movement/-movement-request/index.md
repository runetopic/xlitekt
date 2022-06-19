//[game](../../../index.md)/[xlitekt.game.actor.movement](../index.md)/[MovementRequest](index.md)

# MovementRequest

[jvm]\
data class [MovementRequest](index.md)(val reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?, val waypoints: IntArrayList, val failed: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val alternative: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [MovementRequest](-movement-request.md) | [jvm]<br>fun [MovementRequest](-movement-request.md)(reachAction: () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?, waypoints: IntArrayList, failed: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), alternative: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [alternative](alternative.md) | [jvm]<br>val [alternative](alternative.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [failed](failed.md) | [jvm]<br>val [failed](failed.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [reachAction](reach-action.md) | [jvm]<br>val [reachAction](reach-action.md): () -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [waypoints](waypoints.md) | [jvm]<br>val [waypoints](waypoints.md): IntArrayList |
