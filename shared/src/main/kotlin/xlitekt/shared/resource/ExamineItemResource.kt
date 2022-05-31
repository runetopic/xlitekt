package xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Justin Kenney
 */
@Serializable
data class ExamineItemResource(
    val itemId: Int,
    val message: String
)
