package xlitekt.game.world.map

import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.shared.lazyInject

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
    inline val entry get() = lazyInject<LocEntryTypeProvider>().entryType(id)
    inline val name get() = entry?.name
    inline val angleX get() = location.x + ((if (rotation == 1 || rotation == 3) entry?.height ?: 1 else entry?.width ?: 1) - 1) / 2
    inline val angleZ get() = location.z + ((if (rotation == 1 || rotation == 3) entry?.width ?: 1 else entry?.height ?: 1) - 1) / 2

    override fun toString(): String = "GameObject(id=$id, location=$location, shape=$shape, rotation=$rotation"
}
