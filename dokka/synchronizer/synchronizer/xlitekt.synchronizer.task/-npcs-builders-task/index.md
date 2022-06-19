//[synchronizer](../../../index.md)/[xlitekt.synchronizer.task](../index.md)/[NpcsBuildersTask](index.md)

# NpcsBuildersTask

[jvm]\
class [NpcsBuildersTask](index.md)(builders: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[UpdatesBuilder](../../xlitekt.synchronizer.builder/-updates-builder/index.md)&lt;[NPC](../../../../game/game/xlitekt.game.actor.npc/-n-p-c/index.md), *&gt;&gt;) : [SynchronizerTask](../-synchronizer-task/index.md)&lt;[NPC](../../../../game/game/xlitekt.game.actor.npc/-n-p-c/index.md)&gt; 

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [NpcsBuildersTask](-npcs-builders-task.md) | [jvm]<br>fun [NpcsBuildersTask](-npcs-builders-task.md)(builders: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[UpdatesBuilder](../../xlitekt.synchronizer.builder/-updates-builder/index.md)&lt;[NPC](../../../../game/game/xlitekt.game.actor.npc/-n-p-c/index.md), *&gt;&gt;) |

## Functions

| Name | Summary |
|---|---|
| [execute](execute.md) | [jvm]<br>open override fun [execute](execute.md)(syncPlayers: NonBlockingHashMapLong&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;, actors: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[NPC](../../../../game/game/xlitekt.game.actor.npc/-n-p-c/index.md)&gt;) |
| [finish](finish.md) | [jvm]<br>open override fun [finish](finish.md)() |
