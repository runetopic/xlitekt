//[synchronizer](../../../index.md)/[xlitekt.synchronizer.task](../index.md)/[PlayersBuildersTask](index.md)

# PlayersBuildersTask

[jvm]\
class [PlayersBuildersTask](index.md)(builders: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[UpdatesBuilder](../../xlitekt.synchronizer.builder/-updates-builder/index.md)&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md), *&gt;&gt;) : [SynchronizerTask](../-synchronizer-task/index.md)&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt; 

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [PlayersBuildersTask](-players-builders-task.md) | [jvm]<br>fun [PlayersBuildersTask](-players-builders-task.md)(builders: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[UpdatesBuilder](../../xlitekt.synchronizer.builder/-updates-builder/index.md)&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md), *&gt;&gt;) |

## Functions

| Name | Summary |
|---|---|
| [execute](execute.md) | [jvm]<br>open override fun [execute](execute.md)(syncPlayers: NonBlockingHashMapLong&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;, actors: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;) |
| [finish](finish.md) | [jvm]<br>open override fun [finish](finish.md)() |
