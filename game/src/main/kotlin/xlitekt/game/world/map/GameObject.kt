package xlitekt.game.world.map

import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.shared.inject

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
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

    private companion object {
        val provider by inject<LocEntryTypeProvider>()
    }
}
