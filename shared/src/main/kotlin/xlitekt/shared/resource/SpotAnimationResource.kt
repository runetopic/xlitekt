package xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Jordan Abraham
 */
@Serializable
data class SpotAnimationResource(
    val name: String,
    val id: Int
)
