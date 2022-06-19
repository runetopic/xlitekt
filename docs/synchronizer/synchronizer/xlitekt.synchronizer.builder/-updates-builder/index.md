//[synchronizer](../../../index.md)/[xlitekt.synchronizer.builder](../index.md)/[UpdatesBuilder](index.md)

# UpdatesBuilder

[jvm]\
interface [UpdatesBuilder](index.md)&lt;in [A](index.md) : [Actor](../../../../game/game/xlitekt.game.actor/-actor/index.md), out [T](index.md)&gt;

#### Author

Jordan Abraham

## Functions

| Name | Summary |
|---|---|
| [build](build.md) | [jvm]<br>abstract fun [build](build.md)(syncPlayers: NonBlockingHashMapLong&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;, actor: [A](index.md)) |
| [get](get.md) | [jvm]<br>abstract fun [get](get.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;out [T](index.md)&gt; |
| [reset](reset.md) | [jvm]<br>abstract fun [reset](reset.md)() |

## Inheritors

| Name |
|---|
| [MovementUpdatesBuilder](../-movement-updates-builder/index.md) |
| [NpcHighDefinitionUpdatesBuilder](../-npc-high-definition-updates-builder/index.md) |
| [PlayerAlternativeHighDefinitionUpdatesBuilder](../-player-alternative-high-definition-updates-builder/index.md) |
| [PlayerAlternativeLowDefinitionUpdatesBuilder](../-player-alternative-low-definition-updates-builder/index.md) |
| [PlayerHighDefinitionUpdatesBuilder](../-player-high-definition-updates-builder/index.md) |
| [PlayerLowDefinitionUpdatesBuilder](../-player-low-definition-updates-builder/index.md) |
