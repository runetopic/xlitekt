package com.runetopic.xlitekt.game.item

data class Item(
    val id: Int,
    val amount: Int
) : Comparable<Item> {
    override fun compareTo(item: Item): Int {
        return this.id.compareTo(item.id) + this.amount.compareTo(item.amount)
    }
}
