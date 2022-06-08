package xlitekt.shared.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class ItemSequenceResource(
    @SerialName("itemId")
    val id: Int,
    @SerialName("renderAnimations")
    val renderAnimations: IntArray
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ItemSequenceResource

        if (id != other.id) return false
        if (!renderAnimations.contentEquals(other.renderAnimations)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + renderAnimations.contentHashCode()
        return result
    }
}
