package xlitekt.shared.resource

import kotlinx.serialization.Serializable

@Serializable
data class ExamineNPCResource(
    val message: String,
    val npcID: Int
)
