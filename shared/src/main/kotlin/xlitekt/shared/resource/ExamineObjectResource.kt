package xlitekt.shared.resource

import kotlinx.serialization.Serializable

@Serializable
data class ExamineObjectResource(
    val message: String,
    val objectID: Int
)
