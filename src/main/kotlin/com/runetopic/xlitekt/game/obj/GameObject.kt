package com.runetopic.xlitekt.game.obj

import com.runetopic.xlitekt.cache.provider.config.loc.LocEntryType
import com.runetopic.xlitekt.game.location.Location

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
