package xlitekt.game.content.item

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.shared.inject

private val objEntryTypeProvider by inject<ObjEntryTypeProvider>()

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
@Serializable
data class Item(
    val id: Int,
    val amount: Int
) {
    val entry = objEntryTypeProvider.entryType(id)

    fun isStackable(): Boolean = entry?.isStackable == 1 || isNotable()
    fun isNotable(): Boolean = entry?.noteTemplate != -1
}
