package xlitekt.game.content.item

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.shared.lazy

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
@Serializable
data class Item(
    val id: Int,
    val amount: Int
) {
    inline val entry get() = lazy<ObjEntryTypeProvider>().entryType(id)
    inline val noteable get() = entry?.noteTemplate != -1
    inline val stackable get() = entry?.isStackable == 1 || noteable
}
