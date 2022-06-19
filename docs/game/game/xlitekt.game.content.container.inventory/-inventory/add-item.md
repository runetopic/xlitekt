//[game](../../../index.md)/[xlitekt.game.content.container.inventory](../index.md)/[Inventory](index.md)/[addItem](add-item.md)

# addItem

[jvm]\
fun [addItem](add-item.md)(item: [Item](../../xlitekt.game.content.item/-item/index.md), function: [Item](../../xlitekt.game.content.item/-item/index.md).([Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html))

Adds an item to the player's inventory, and if the item is added we invoke a function with the slotId of the newly added item.

[jvm]\
fun [addItem](add-item.md)(item: [Item](../../xlitekt.game.content.item/-item/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)

Add an item to the player's inventory and sends the client a message if the item cannot be added.

#### Return

whether of not the item has been added to the inventory.

## Parameters

jvm

| | |
|---|---|
| item | The item being added to the inventory. |
