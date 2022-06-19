//[game](../../../index.md)/[xlitekt.game.queue](../index.md)/[QueuedScriptList](index.md)

# QueuedScriptList

[jvm]\
abstract class [QueuedScriptList](index.md)&lt;[T](index.md) : [Actor](../../xlitekt.game.actor/-actor/index.md)&gt;(queue: [LinkedList](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; = LinkedList()) : [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; 

This is a base class used for storing all the queued scripts for Actors.

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [QueuedScriptList](-queued-script-list.md) | [jvm]<br>fun &lt;[T](index.md) : [Actor](../../xlitekt.game.actor/-actor/index.md)&gt; [QueuedScriptList](-queued-script-list.md)(queue: [LinkedList](https://docs.oracle.com/javase/8/docs/api/java/util/LinkedList.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; = LinkedList()) |

## Functions

| Name | Summary |
|---|---|
| [cancelAllScripts](cancel-all-scripts.md) | [jvm]<br>fun [cancelAllScripts](cancel-all-scripts.md)()<br>Removes all the scripts in the queue and terminates them. |
| [cancelWeakScripts](cancel-weak-scripts.md) | [jvm]<br>fun [cancelWeakScripts](cancel-weak-scripts.md)()<br>Removes all weak scripts in the queue and terminates them. |
| [contains](index.md#-1940617017%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [contains](index.md#-1940617017%2FFunctions%2F440369633)(element: [QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#1869942136%2FFunctions%2F440369633) | [jvm]<br>open override fun [containsAll](index.md#1869942136%2FFunctions%2F440369633)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.md#1146829764%2FFunctions%2F440369633) | [jvm]<br>open fun [forEach](index.md#1146829764%2FFunctions%2F440369633)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt;) |
| [get](index.md#961975567%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [get](index.md#961975567%2FFunctions%2F440369633)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt; |
| [indexOf](index.md#-1054063075%2FFunctions%2F440369633) | [jvm]<br>open override fun [indexOf](index.md#-1054063075%2FFunctions%2F440369633)(element: [QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](index.md#-1000881820%2FFunctions%2F440369633) | [jvm]<br>open override fun [isEmpty](index.md#-1000881820%2FFunctions%2F440369633)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](index.md#-1577986619%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [iterator](index.md#-1577986619%2FFunctions%2F440369633)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; |
| [lastIndexOf](index.md#-762674137%2FFunctions%2F440369633) | [jvm]<br>open override fun [lastIndexOf](index.md#-762674137%2FFunctions%2F440369633)(element: [QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [listIterator](index.md#-236165689%2FFunctions%2F440369633) | [jvm]<br>open override fun [listIterator](index.md#-236165689%2FFunctions%2F440369633)(): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt;<br>open override fun [listIterator](index.md#845091493%2FFunctions%2F440369633)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; |
| [parallelStream](index.md#-1592339412%2FFunctions%2F440369633) | [jvm]<br>open fun [parallelStream](index.md#-1592339412%2FFunctions%2F440369633)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; |
| [process](process.md) | [jvm]<br>abstract fun [process](process.md)(actor: [T](index.md)) |
| [queue](queue.md) | [jvm]<br>fun [queue](queue.md)(executor: [T](index.md), priority: [QueuedScriptPriority](../-queued-script-priority/index.md), suspendedScript: [SuspendableQueuedScript](../index.md#1908705368%2FClasslikes%2F440369633)&lt;[T](index.md)&gt;)<br>This function queues a script up in the script list. |
| [spliterator](index.md#703021258%2FFunctions%2F440369633) | [jvm]<br>open override fun [spliterator](index.md#703021258%2FFunctions%2F440369633)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; |
| [stream](index.md#135225651%2FFunctions%2F440369633) | [jvm]<br>open fun [stream](index.md#135225651%2FFunctions%2F440369633)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; |
| [subList](index.md#423386006%2FFunctions%2F440369633) | [jvm]<br>open override fun [subList](index.md#423386006%2FFunctions%2F440369633)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[QueuedScript](../-queued-script/index.md)&lt;[T](index.md)&gt;&gt; |
| [takeInput](take-input.md) | [jvm]<br>fun [takeInput](take-input.md)(value: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html))<br>Takes the last input from the first item in the queue. This does not remove the item from the queue. |
| [toArray](index.md#-1215154575%2FFunctions%2F440369633) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](index.md#-1215154575%2FFunctions%2F440369633) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](index.md#-1215154575%2FFunctions%2F440369633)~~(~~p0: [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-1215154575%2FFunctions%2F440369633)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](index.md#-1215154575%2FFunctions%2F440369633)&gt; |

## Properties

| Name | Summary |
|---|---|
| [size](index.md#844915858%2FProperties%2F440369633) | [jvm]<br>open override val [size](index.md#844915858%2FProperties%2F440369633): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Inheritors

| Name |
|---|
| [ActorScriptQueue](../-actor-script-queue/index.md) |
