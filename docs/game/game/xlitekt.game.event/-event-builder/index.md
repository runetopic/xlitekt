//[game](../../../index.md)/[xlitekt.game.event](../index.md)/[EventBuilder](index.md)

# EventBuilder

[jvm]\
class [EventBuilder](index.md)&lt;[T](index.md) : [Event](../-event/index.md)&gt;(events: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[EventListener](../-event-listener/index.md)&lt;*&gt;&gt;)

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [EventBuilder](-event-builder.md) | [jvm]<br>fun [EventBuilder](-event-builder.md)(events: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[EventListener](../-event-listener/index.md)&lt;*&gt;&gt;) |

## Functions

| Name | Summary |
|---|---|
| [filter](filter.md) | [jvm]<br>fun [filter](filter.md)(predicate: [T](index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [EventBuilder](index.md)&lt;[T](index.md)&gt; |
| [use](use.md) | [jvm]<br>fun [use](use.md)(use: [T](index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
