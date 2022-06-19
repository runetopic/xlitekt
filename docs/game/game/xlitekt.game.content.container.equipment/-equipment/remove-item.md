//[game](../../../index.md)/[xlitekt.game.content.container.equipment](../index.md)/[Equipment](index.md)/[removeItem](remove-item.md)

# removeItem

[jvm]\
fun [removeItem](remove-item.md)(slotId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), item: [Item](../../xlitekt.game.content.item/-item/index.md), amount: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = item.amount, function: [Item](../../xlitekt.game.content.item/-item/index.md).([Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Removes an item from the equipment container.

## Parameters

jvm

| | |
|---|---|
| slotId | The slot id that we want to remove it from. |
| item | The item we're removing from the container. |
| amount | The amount that we want to remove from the container. |
| function | This is the function we invoke if we successfully removed an item. |
