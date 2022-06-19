//[synchronizer](../../../index.md)/[xlitekt.synchronizer.task](../index.md)/[SynchronizerTask](index.md)

# SynchronizerTask

[jvm]\
interface [SynchronizerTask](index.md)&lt;[A](index.md) : [Actor](../../../../game/game/xlitekt.game.actor/-actor/index.md)&gt;

#### Author

Jordan Abraham

## Functions

| Name | Summary |
|---|---|
| [execute](execute.md) | [jvm]<br>abstract fun [execute](execute.md)(syncPlayers: NonBlockingHashMapLong&lt;[Player](../../../../game/game/xlitekt.game.actor.player/-player/index.md)&gt;, actors: [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[A](index.md)&gt;) |
| [finish](finish.md) | [jvm]<br>abstract fun [finish](finish.md)() |

## Inheritors

| Name |
|---|
| [ClientsSynchronizerTask](../-clients-synchronizer-task/index.md) |
| [LoginsSynchronizerTask](../-logins-synchronizer-task/index.md) |
| [LogoutsSynchronizerTask](../-logouts-synchronizer-task/index.md) |
| [NpcsBuildersTask](../-npcs-builders-task/index.md) |
| [NpcsSynchronizerTask](../-npcs-synchronizer-task/index.md) |
| [PlayersBuildersTask](../-players-builders-task/index.md) |
| [PlayersSynchronizerTask](../-players-synchronizer-task/index.md) |
| [ZonesSynchronizerTask](../-zones-synchronizer-task/index.md) |
