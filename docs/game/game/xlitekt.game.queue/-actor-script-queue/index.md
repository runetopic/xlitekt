//[game](../../../index.md)/[xlitekt.game.queue](../index.md)/[ActorScriptQueue](index.md)

# ActorScriptQueue

[jvm]\
class [ActorScriptQueue](index.md) : [QueuedScriptList](../-queued-script-list/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt; 

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [ActorScriptQueue](-actor-script-queue.md) | [jvm]<br>fun [ActorScriptQueue](-actor-script-queue.md)() |

## Functions

| Name | Summary |
|---|---|
| [cancelAllScripts](../-queued-script-list/cancel-all-scripts.md) | [jvm]<br>fun [cancelAllScripts](../-queued-script-list/cancel-all-scripts.md)()<br>Removes all the scripts in the queue and terminates them. |
| [cancelWeakScripts](../-queued-script-list/cancel-weak-scripts.md) | [jvm]<br>fun [cancelWeakScripts](../-queued-script-list/cancel-weak-scripts.md)()<br>Removes all weak scripts in the queue and terminates them. |
| [contains](index.md#-176899661%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [contains](index.md#-176899661%2FFunctions%2F440369633)(element: [QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#612051724%2FFunctions%2F440369633) | [jvm]<br>open override fun [containsAll](index.md#612051724%2FFunctions%2F440369633)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.md#-292128936%2FFunctions%2F440369633) | [jvm]<br>open fun [forEach](index.md#-292128936%2FFunctions%2F440369633)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt;) |
| [get](../-queued-script-list/index.md#961975567%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [get](../-queued-script-list/index.md#961975567%2FFunctions%2F440369633)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt; |
| [indexOf](index.md#-1556873719%2FFunctions%2F440369633) | [jvm]<br>open override fun [indexOf](index.md#-1556873719%2FFunctions%2F440369633)(element: [QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](../-queued-script-list/index.md#-1000881820%2FFunctions%2F440369633) | [jvm]<br>open override fun [isEmpty](../-queued-script-list/index.md#-1000881820%2FFunctions%2F440369633)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](../-queued-script-list/index.md#-1577986619%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [iterator](../-queued-script-list/index.md#-1577986619%2FFunctions%2F440369633)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt; |
| [lastIndexOf](index.md#1412572435%2FFunctions%2F440369633) | [jvm]<br>open override fun [lastIndexOf](index.md#1412572435%2FFunctions%2F440369633)(element: [QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [listIterator](../-queued-script-list/index.md#-236165689%2FFunctions%2F440369633) | [jvm]<br>open override fun [listIterator](../-queued-script-list/index.md#-236165689%2FFunctions%2F440369633)(): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt;<br>open override fun [listIterator](../-queued-script-list/index.md#845091493%2FFunctions%2F440369633)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt; |
| [parallelStream](../-queued-script-list/index.md#-1592339412%2FFunctions%2F440369633) | [jvm]<br>open fun [parallelStream](../-queued-script-list/index.md#-1592339412%2FFunctions%2F440369633)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt; |
| [process](process.md) | [jvm]<br>open override fun [process](process.md)(actor: [Actor](../../xlitekt.game.actor/-actor/index.md))<br>This will process all the queued script items and handle the suspension and priority accordingly. |
| [queue](index.md#-1396126457%2FFunctions%2F440369633) | [jvm]<br>fun [queue](index.md#-1396126457%2FFunctions%2F440369633)(executor: [Actor](../../xlitekt.game.actor/-actor/index.md), priority: [QueuedScriptPriority](../-queued-script-priority/index.md), suspendedScript: [SuspendableQueuedScript](../index.md#1908705368%2FClasslikes%2F440369633)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;)<br>This function queues a script up in the script list. |
| [spliterator](../-queued-script-list/index.md#703021258%2FFunctions%2F440369633) | [jvm]<br>open override fun [spliterator](../-queued-script-list/index.md#703021258%2FFunctions%2F440369633)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt; |
| [stream](../-queued-script-list/index.md#135225651%2FFunctions%2F440369633) | [jvm]<br>open fun [stream](../-queued-script-list/index.md#135225651%2FFunctions%2F440369633)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt; |
| [subList](../-queued-script-list/index.md#423386006%2FFunctions%2F440369633) | [jvm]<br>open override fun [subList](../-queued-script-list/index.md#423386006%2FFunctions%2F440369633)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[Actor](../../xlitekt.game.actor/-actor/index.md)&gt;&gt; |
| [takeInput](../-queued-script-list/take-input.md) | [jvm]<br>fun [takeInput](../-queued-script-list/take-input.md)(value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html))<br>Takes the last input from the first item in the queue. This does not remove the item from the queue. |
| [toArray](../-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](../-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](../-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633)~~(~~p0: [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633)&gt; |

## Properties

| Name | Summary |
|---|---|
| [size](../-queued-script-list/index.md#844915858%2FProperties%2F440369633) | [jvm]<br>open override val [size](../-queued-script-list/index.md#844915858%2FProperties%2F440369633): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
