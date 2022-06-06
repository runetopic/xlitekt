package xlitekt.game.world.map

import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.shared.lazy

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
data class GameObject(
    val id: Int,
    val location: Location,
    val shape: Int,
    val rotation: Int
) {
    inline val entry get() = lazy<LocEntryTypeProvider>().entryType(id)!!
    inline val name get() = entry.name

    override fun toString(): String = "GameObject(id=$id, location=$location, shape=$shape, rotation=$rotation"
}
