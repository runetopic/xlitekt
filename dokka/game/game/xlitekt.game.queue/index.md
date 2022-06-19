//[game](../../index.md)/[xlitekt.game.queue](index.md)

# Package xlitekt.game.queue

## Types

| Name | Summary |
|---|---|
| [ActorScriptQueue](-actor-script-queue/index.md) | [jvm]<br>class [ActorScriptQueue](-actor-script-queue/index.md) : [QueuedScriptList](-queued-script-list/index.md)&lt;[Actor](../xlitekt.game.actor/-actor/index.md)&gt; |
| [QueuedScript](-queued-script/index.md) | [jvm]<br>data class [QueuedScript](-queued-script/index.md)&lt;[T](-queued-script/index.md) : [Actor](../xlitekt.game.actor/-actor/index.md)&gt;(executor: [T](-queued-script/index.md), val priority: [QueuedScriptPriority](-queued-script-priority/index.md)) : [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; |
| [QueuedScriptConditions](-queued-script-conditions/index.md) | [jvm]<br>sealed class [QueuedScriptConditions](-queued-script-conditions/index.md)<br>This class represents the possible script conditions. |
| [QueuedScriptList](-queued-script-list/index.md) | [jvm]<br>abstract class [QueuedScriptList](-queued-script-list/index.md)&lt;[T](-queued-script-list/index.md) : [Actor](../xlitekt.game.actor/-actor/index.md)&gt;(queue: [LinkedList](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)&lt;[QueuedScript](-queued-script/index.md)&lt;[T](-queued-script-list/index.md)&gt;&gt; = LinkedList()) : [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[QueuedScript](-queued-script/index.md)&lt;[T](-queued-script-list/index.md)&gt;&gt; <br>This is a base class used for storing all the queued scripts for Actors. |
| [QueuedScriptPriority](-queued-script-priority/index.md) | [jvm]<br>sealed class [QueuedScriptPriority](-queued-script-priority/index.md)<br>This class represents all the possible queue priority types. |
| [QueuedScriptStage](-queued-script-stage/index.md) | [jvm]<br>data class [QueuedScriptStage](-queued-script-stage/index.md)(val condition: [QueuedScriptConditions](-queued-script-conditions/index.md), val continuation: [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;)<br>This class represents the queued scripts current stage. |
| [QueuedScriptUnit](index.md#-1567077484%2FClasslikes%2F440369633) | [jvm]<br>typealias [QueuedScriptUnit](index.md#-1567077484%2FClasslikes%2F440369633)&lt;[T](index.md#-1567077484%2FClasslikes%2F440369633)&gt; = [QueuedScript](-queued-script/index.md)&lt;[T](index.md#-1567077484%2FClasslikes%2F440369633)&gt;.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
| [SuspendableQueuedScript](index.md#1908705368%2FClasslikes%2F440369633) | [jvm]<br>typealias [SuspendableQueuedScript](index.md#1908705368%2FClasslikes%2F440369633)&lt;[T](index.md#1908705368%2FClasslikes%2F440369633)&gt; = suspend [QueuedScript](-queued-script/index.md)&lt;[T](index.md#1908705368%2FClasslikes%2F440369633)&gt;.(CoroutineScope) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |

## Properties

| Name | Summary |
|---|---|
| [scriptDispatcher](script-dispatcher.md) | [jvm]<br>val [scriptDispatcher](script-dispatcher.md): ExecutorCoroutineDispatcher |
