package com.runetopic.xlitekt.game.item

data class Item(
    val id: Int,
    val amount: Int
) : Comparable<Item> {
    override fun compareTo(other: Item): Int {
        return this.id.compareTo(other.id) + this.amount.compareTo(other.amount)
    }
}
