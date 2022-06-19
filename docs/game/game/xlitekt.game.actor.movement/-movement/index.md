//[game](../../../index.md)/[xlitekt.game.actor.movement](../index.md)/[Movement](index.md)

# Movement

[jvm]\
class [Movement](index.md)

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [Movement](-movement.md) | [jvm]<br>fun [Movement](-movement.md)() |

## Functions

| Name | Summary |
|---|---|
| [isMoving](is-moving.md) | [jvm]<br>fun [isMoving](is-moving.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [process](process.md) | [jvm]<br>fun [process](process.md)(actor: [Actor](../../xlitekt.game.actor/-actor/index.md)): [MovementStep](../-movement-step/index.md)?<br>Process any pending movement for a actor. |
| [reset](reset.md) | [jvm]<br>fun [reset](reset.md)()<br>Resets this movement. |
| [route](route.md) | [jvm]<br>fun [route](route.md)(request: [MovementRequest](../-movement-request/index.md))<br>Routes this movement to a movement request. Queues checkpoints from this movement request.<br>[jvm]<br>fun [route](route.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md), teleport: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))<br>Routes this movement to a single location with optional teleport speed. |

## Properties

| Name | Summary |
|---|---|
| [movementRequest](movement-request.md) | [jvm]<br>var [movementRequest](movement-request.md): [Optional](https://docs.oracle.com/javase/8/docs/api/java/util/Optional.html)&lt;[MovementRequest](../-movement-request/index.md)&gt; |
| [movementSpeed](movement-speed.md) | [jvm]<br>var [movementSpeed](movement-speed.md): [MovementSpeed](../-movement-speed/index.md) |
