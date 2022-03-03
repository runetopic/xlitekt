package xlitekt.game.world.map.obj

import xlitekt.cache.provider.config.loc.LocEntryType
import xlitekt.game.world.map.location.Location

data class GameObject(
    val entry: LocEntryType,
    val location: Location,
    val shape: Int,
    val rotation: Int
) {
    val id: Int get() = entry.id
    val name: String get() = entry.name

    override fun toString(): String = "GameObject(entry=$entry, location=$location, shape=$shape, roation=$rotation"
}
