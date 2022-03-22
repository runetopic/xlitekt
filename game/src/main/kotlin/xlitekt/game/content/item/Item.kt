package xlitekt.game.content.item

open class Item(
    open val id: Int,
    open val amount: Int
) : Comparable<Item> {
    override fun compareTo(other: Item): Int {
        return this.id.compareTo(other.id) + this.amount.compareTo(other.amount)
    }
}
