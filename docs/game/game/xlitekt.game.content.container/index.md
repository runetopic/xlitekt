//[game](../../index.md)/[xlitekt.game.content.container](index.md)

# Package xlitekt.game.content.container

## Types

| Name | Summary |
|---|---|
| [Container](-container/index.md) | [jvm]<br>abstract class [Container](-container/index.md)(val id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val capacity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), items: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[Item](../xlitekt.game.content.item/-item/index.md)?&gt; = MutableList(capacity) { null }, val alwaysStack: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false) : [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Item](../xlitekt.game.content.item/-item/index.md)?&gt; <br>This class represents all the items stored on a specific RS container. |
| [ContainerUpdate](index.md#-1938570009%2FClasslikes%2F440369633) | [jvm]<br>typealias [ContainerUpdate](index.md#-1938570009%2FClasslikes%2F440369633) = [Item](../xlitekt.game.content.item/-item/index.md).([List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)&gt;) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html) |
