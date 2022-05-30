package xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Justin Kenney
 */
@Serializable
data class ExamineNPCResource(
    val message: String,
    val npcID: Int
)
