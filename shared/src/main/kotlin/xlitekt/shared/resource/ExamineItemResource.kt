package xlitekt.shared.resource

import kotlinx.serialization.Serializable

@Serializable
data class ExamineItemResource(
    val itemID: Int,
    val message: String
)
