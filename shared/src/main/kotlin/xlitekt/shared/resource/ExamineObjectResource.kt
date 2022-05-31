package xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Justin Kenney
 */
@Serializable
data class ExamineObjectResource(
    val message: String,
    val objectId: Int
)
