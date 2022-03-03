package xlitekt.shared.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapSquareResource(
    val archive: Int,
    val group: Int,
    @SerialName("name_hash")
    val nameHash: Int,
    val name: String,
    val mapsquare: Int,
    val key: List<Int>
)
