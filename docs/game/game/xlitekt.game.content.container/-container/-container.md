//[game](../../../index.md)/[xlitekt.game.content.container](../index.md)/[Container](index.md)/[Container](-container.md)

# Container

[jvm]\
fun [Container](-container.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), capacity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), items: [MutableList](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-list/index.html)&lt;[Item](../../xlitekt.game.content.item/-item/index.md)?&gt; = MutableList(capacity) { null }, alwaysStack: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false)

## Parameters

jvm

| | |
|---|---|
| id | The id of the container. (E.g: Container key ids in the cache) |
| capacity | The capacity of the container. |
| alwaysStack | If this container is always stacking items within the container. (This currently has limited support. Will be adding more in the future) |
