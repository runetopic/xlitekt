package com.runetopic.xlitekt.util.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapSquare(
    @SerialName("mapsquare")
    val regionId: Int,
    @SerialName("key")
    val keys: List<Int>
) : ArrayList<MapSquare>()
