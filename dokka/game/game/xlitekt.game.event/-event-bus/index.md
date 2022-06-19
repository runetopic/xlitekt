//[game](../../../index.md)/[xlitekt.game.event](../index.md)/[EventBus](index.md)

# EventBus

[jvm]\
class [EventBus](index.md)

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [EventBus](-event-bus.md) | [jvm]<br>fun [EventBus](-event-bus.md)() |

## Functions

| Name | Summary |
|---|---|
| [notify](notify.md) | [jvm]<br>inline fun &lt;[T](notify.md) : [Event](../-event/index.md)&gt; [notify](notify.md)(event: [T](notify.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [onEvent](on-event.md) | [jvm]<br>inline fun &lt;[T](on-event.md) : [Event](../-event/index.md)&gt; [onEvent](on-event.md)(): [EventBuilder](../-event-builder/index.md)&lt;[T](on-event.md)&gt; |

## Properties

| Name | Summary |
|---|---|
| [events](events.md) | [jvm]<br>val [events](events.md): [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;out [Event](../-event/index.md)&gt;, [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[EventListener](../-event-listener/index.md)&lt;*&gt;&gt;&gt; |
