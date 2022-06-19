//[synchronizer](../../../index.md)/[xlitekt.synchronizer.builder](../index.md)/[MovementUpdatesBuilder](index.md)

# MovementUpdatesBuilder

[jvm]\
class [MovementUpdatesBuilder](index.md)&lt;in [A](index.md) : [Actor](../../../../game/game/xlitekt.game.actor/-actor/index.md)&gt; : [UpdatesBuilder](../-updates-builder/index.md)&lt;[A](index.md), [MovementStep](../../../../game/game/xlitekt.game.actor.movement/-movement-step/index.md)?&gt; 

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [MovementUpdatesBuilder](-movement-updates-builder.md) | [jvm]<br>fun [MovementUpdatesBuilder](-movement-updates-builder.md)() |

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [jvm]<br>open override fun [build](build.md)(syncPlayers: NonBlockingHashMapLong&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;, actor: [A](index.md)) |
| [get](get.md) | [jvm]<br>open override fun [get](get.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MovementStep](../../../../game/game/xlitekt.game.actor.movement/-movement-step/index.md)?&gt; |
| [reset](reset.md) | [jvm]<br>open override fun [reset](reset.md)() |
