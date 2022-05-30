package xlitekt.game.world.map.obj

import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject

private val provider by inject<LocEntryTypeProvider>()

data class GameObject(
    val id: Int,
    val location: Location,
    val shape: Int,
    val rotation: Int,
    val spawned: Boolean = true
) {
    val entry = provider.entryType(id)!!
    val name = entry.name

    override fun toString(): String = "GameObject(id=$id, location=$location, shape=$shape, rotation=$rotation"
}
