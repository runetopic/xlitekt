//[synchronizer](../../../index.md)/[xlitekt.synchronizer.task](../index.md)/[ClientsSynchronizerTask](index.md)

# ClientsSynchronizerTask

[jvm]\
class [ClientsSynchronizerTask](index.md)(playerMovementUpdatesBuilder: [MovementUpdatesBuilder](../../xlitekt.synchronizer.builder/-movement-updates-builder/index.md)&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;, playerHighDefinitionUpdatesBuilder: [PlayerHighDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-player-high-definition-updates-builder/index.md), playerLowDefinitionUpdatesBuilder: [PlayerLowDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-player-low-definition-updates-builder/index.md), playerAlternativeHighDefinitionUpdatesBuilder: [PlayerAlternativeHighDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-player-alternative-high-definition-updates-builder/index.md), playerAlternativeLowDefinitionUpdatesBuilder: [PlayerAlternativeLowDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-player-alternative-low-definition-updates-builder/index.md), npcHighDefinitionUpdatesBuilder: [NpcHighDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-npc-high-definition-updates-builder/index.md), npcMovementUpdatesBuilder: [MovementUpdatesBuilder](../../xlitekt.synchronizer.builder/-movement-updates-builder/index.md)&lt;[NPC](../../../../game/game/xlitekt.game.actor.npc/-n-p-c/index.md)&gt;) : [SynchronizerTask](../-synchronizer-task/index.md)&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt; 

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [ClientsSynchronizerTask](-clients-synchronizer-task.md) | [jvm]<br>fun [ClientsSynchronizerTask](-clients-synchronizer-task.md)(playerMovementUpdatesBuilder: [MovementUpdatesBuilder](../../xlitekt.synchronizer.builder/-movement-updates-builder/index.md)&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;, playerHighDefinitionUpdatesBuilder: [PlayerHighDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-player-high-definition-updates-builder/index.md), playerLowDefinitionUpdatesBuilder: [PlayerLowDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-player-low-definition-updates-builder/index.md), playerAlternativeHighDefinitionUpdatesBuilder: [PlayerAlternativeHighDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-player-alternative-high-definition-updates-builder/index.md), playerAlternativeLowDefinitionUpdatesBuilder: [PlayerAlternativeLowDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-player-alternative-low-definition-updates-builder/index.md), npcHighDefinitionUpdatesBuilder: [NpcHighDefinitionUpdatesBuilder](../../xlitekt.synchronizer.builder/-npc-high-definition-updates-builder/index.md), npcMovementUpdatesBuilder: [MovementUpdatesBuilder](../../xlitekt.synchronizer.builder/-movement-updates-builder/index.md)&lt;[NPC](../../../../game/game/xlitekt.game.actor.npc/-n-p-c/index.md)&gt;) |

## Functions

| Name | Summary |
|---|---|
| [execute](execute.md) | [jvm]<br>open override fun [execute](execute.md)(syncPlayers: NonBlockingHashMapLong&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;, actors: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;) |
| [finish](finish.md) | [jvm]<br>open override fun [finish](finish.md)() |
