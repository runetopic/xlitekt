package xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Justin Kenney
 */
@Serializable
data class ExamineItemResource(
    val itemID: Int,
    val message: String
)
