//[game](../../../index.md)/[xlitekt.game.actor](../index.md)/[ActorList](index.md)

# ActorList

[jvm]\
class [ActorList](index.md)&lt;[T](index.md)&gt;(initialCapacity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), actors: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[T](index.md)?&gt; = createMutableList&lt;T?&gt;(initialCapacity)) : [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)?&gt; 

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [ActorList](-actor-list.md) | [jvm]<br>fun &lt;[T](index.md)&gt; [ActorList](-actor-list.md)(initialCapacity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), actors: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[T](index.md)?&gt; = createMutableList&lt;T?&gt;(initialCapacity)) |

## Functions

| Name | Summary |
|---|---|
| [add](add.md) | [jvm]<br>fun [add](add.md)(actor: [Actor](../-actor/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [contains](index.md#-995005991%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [contains](index.md#-995005991%2FFunctions%2F440369633)(element: [T](index.md)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [containsAll](index.md#-1499100238%2FFunctions%2F440369633) | [jvm]<br>open override fun [containsAll](index.md#-1499100238%2FFunctions%2F440369633)(elements: [Collection](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-collection/index.html)&lt;[T](index.md)?&gt;): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [forEach](index.md#1465639398%2FFunctions%2F440369633) | [jvm]<br>open fun [forEach](index.md#1465639398%2FFunctions%2F440369633)(p0: [Consumer](https://docs.oracle.com/javase/8/docs/api/java/util/function/Consumer.html)&lt;in [T](index.md)?&gt;) |
| [get](../../xlitekt.game.queue/-queued-script-list/index.md#961975567%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [get](../../xlitekt.game.queue/-queued-script-list/index.md#961975567%2FFunctions%2F440369633)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [T](index.md)? |
| [indexOf](index.md#1271455939%2FFunctions%2F440369633) | [jvm]<br>open override fun [indexOf](index.md#1271455939%2FFunctions%2F440369633)(element: [T](index.md)?): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isEmpty](is-empty.md) | [jvm]<br>open override fun [isEmpty](is-empty.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [iterator](../../xlitekt.game.queue/-queued-script-list/index.md#-1577986619%2FFunctions%2F440369633) | [jvm]<br>open operator override fun [iterator](../../xlitekt.game.queue/-queued-script-list/index.md#-1577986619%2FFunctions%2F440369633)(): [Iterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-iterator/index.html)&lt;[T](index.md)?&gt; |
| [lastIndexOf](index.md#-758058375%2FFunctions%2F440369633) | [jvm]<br>open override fun [lastIndexOf](index.md#-758058375%2FFunctions%2F440369633)(element: [T](index.md)?): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [listIterator](../../xlitekt.game.queue/-queued-script-list/index.md#-236165689%2FFunctions%2F440369633) | [jvm]<br>open override fun [listIterator](../../xlitekt.game.queue/-queued-script-list/index.md#-236165689%2FFunctions%2F440369633)(): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[T](index.md)?&gt;<br>open override fun [listIterator](../../xlitekt.game.queue/-queued-script-list/index.md#845091493%2FFunctions%2F440369633)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [ListIterator](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list-iterator/index.html)&lt;[T](index.md)?&gt; |
| [parallelStream](../../xlitekt.game.queue/-queued-script-list/index.md#-1592339412%2FFunctions%2F440369633) | [jvm]<br>open fun [parallelStream](../../xlitekt.game.queue/-queued-script-list/index.md#-1592339412%2FFunctions%2F440369633)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.md)?&gt; |
| [remove](remove.md) | [jvm]<br>fun [remove](remove.md)(actor: [Actor](../-actor/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [spliterator](../../xlitekt.game.queue/-queued-script-list/index.md#703021258%2FFunctions%2F440369633) | [jvm]<br>open override fun [spliterator](../../xlitekt.game.queue/-queued-script-list/index.md#703021258%2FFunctions%2F440369633)(): [Spliterator](https://docs.oracle.com/javase/8/docs/api/java/util/Spliterator.html)&lt;[T](index.md)?&gt; |
| [stream](../../xlitekt.game.queue/-queued-script-list/index.md#135225651%2FFunctions%2F440369633) | [jvm]<br>open fun [stream](../../xlitekt.game.queue/-queued-script-list/index.md#135225651%2FFunctions%2F440369633)(): [Stream](https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html)&lt;[T](index.md)?&gt; |
| [subList](../../xlitekt.game.queue/-queued-script-list/index.md#423386006%2FFunctions%2F440369633) | [jvm]<br>open override fun [subList](../../xlitekt.game.queue/-queued-script-list/index.md#423386006%2FFunctions%2F440369633)(fromIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), toIndex: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[T](index.md)?&gt; |
| [toArray](../../xlitekt.game.queue/-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633) | [jvm]<br>~~open~~ ~~fun~~ ~~&lt;~~[T](../../xlitekt.game.queue/-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633) : [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)~~&gt;~~ [~~toArray~~](../../xlitekt.game.queue/-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633)~~(~~p0: [IntFunction](https://docs.oracle.com/javase/8/docs/api/java/util/function/IntFunction.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../../xlitekt.game.queue/-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633)&gt;&gt;~~)~~~~:~~ [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[T](../../xlitekt.game.queue/-queued-script-list/index.md#-1215154575%2FFunctions%2F440369633)&gt; |

## Properties

| Name | Summary |
|---|---|
| [capacity](capacity.md) | [jvm]<br>val [capacity](capacity.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [indices](indices.md) | [jvm]<br>val [indices](indices.md): [IntRange](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.ranges/-int-range/index.html) |
| [size](size.md) | [jvm]<br>open override val [size](size.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
