package xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class VarInfoResource(
    val id: Int,
    val name: String
)
