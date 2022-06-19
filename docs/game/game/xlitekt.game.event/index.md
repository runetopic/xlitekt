//[game](../../index.md)/[xlitekt.game.event](index.md)

# Package xlitekt.game.event

## Types

| Name | Summary |
|---|---|
| [Event](-event/index.md) | [jvm]<br>interface [Event](-event/index.md) |
| [EventBuilder](-event-builder/index.md) | [jvm]<br>class [EventBuilder](-event-builder/index.md)&lt;[T](-event-builder/index.md) : [Event](-event/index.md)&gt;(events: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[EventListener](-event-listener/index.md)&lt;*&gt;&gt;) |
| [EventBus](-event-bus/index.md) | [jvm]<br>class [EventBus](-event-bus/index.md) |
| [EventListener](-event-listener/index.md) | [jvm]<br>data class [EventListener](-event-listener/index.md)&lt;[T](-event-listener/index.md) : [Event](-event/index.md)&gt;(var use: [T](-event-listener/index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), var filter: [T](-event-listener/index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [onEvent](on-event.md) | [jvm]<br>inline fun &lt;[T](on-event.md) : [Event](-event/index.md)&gt; [onEvent](on-event.md)(noinline function: [T](on-event.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
