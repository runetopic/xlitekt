//[game](../../../index.md)/[xlitekt.game.event](../index.md)/[EventListener](index.md)

# EventListener

[jvm]\
data class [EventListener](index.md)&lt;[T](index.md) : [Event](../-event/index.md)&gt;(var use: [T](index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), var filter: [T](index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [EventListener](-event-listener.md) | [jvm]<br>fun &lt;[T](index.md) : [Event](../-event/index.md)&gt; [EventListener](-event-listener.md)(use: [T](index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html), filter: [T](index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |

## Properties

| Name | Summary |
|---|---|
| [filter](filter.md) | [jvm]<br>var [filter](filter.md): [T](index.md).() -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [use](use.md) | [jvm]<br>var [use](use.md): [T](index.md).() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
