package com.runetopic.xlitekt.game.map

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapSquare(
    @SerialName("mapsquare")
    val regionId: Int,
    @SerialName("key")
    val keys: List<Int>
)
