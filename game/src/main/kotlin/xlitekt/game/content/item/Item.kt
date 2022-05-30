package xlitekt.game.content.item

import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.shared.inject

private val objEntryTypeProvider by inject<ObjEntryTypeProvider>()

data class Item(
    val id: Int,
    val amount: Int
) : Comparable<Item> {
    val entry = objEntryTypeProvider.entryType(id)

    fun isStackable(): Boolean = entry?.isStackable == 1 || isNotable()
    fun isNotable(): Boolean = entry?.noteTemplate != -1

    override fun compareTo(other: Item): Int {
        return this.id.compareTo(other.id) + this.amount.compareTo(other.amount)
    }
}
