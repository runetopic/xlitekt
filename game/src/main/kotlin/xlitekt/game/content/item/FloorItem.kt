package xlitekt.game.content.item

import xlitekt.game.world.map.location.Location

data class FloorItem(
    val id: Int,
    val amount: Int,
    val location: Location
) : Comparable<FloorItem> {
    override fun compareTo(other: FloorItem): Int {
        return this.id.compareTo(other.id) + this.amount.compareTo(other.amount)
    }
}
