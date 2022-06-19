//[game](../../../index.md)/[xlitekt.game.actor.movement](../index.md)/[Movement](index.md)/[route](route.md)

# route

[jvm]\
fun [route](route.md)(request: [MovementRequest](../-movement-request/index.md))

Routes this movement to a movement request. Queues checkpoints from this movement request.

## Parameters

jvm

| | |
|---|---|
| request | The movement request. |

[jvm]\
fun [route](route.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md), teleport: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))

Routes this movement to a single location with optional teleport speed.
