package xlitekt.cache.provider.config.sequence

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
@Serializable
data class SequenceEntryType(
    override val id: Int,
    var field2079: Int = -1,
    var field2082: Int = 0,
    var field2083: Int = 0,
    var frameCount: Int = -1,
    var field2091: Boolean = false,
    var field2092: Int = 5,
    var shield: Int = -1,
    var weapon: Int = -1,
    var field2095: Int = 99,
    var field2096: Int = -1,
    var field2097: Int = -1,
    var field2078: Int = 2,
    var frameLengths: List<Int> = listOf(),
    var frameIds: List<Int> = listOf(),
    var chatFrameIds: List<Int> = listOf()
) : EntryType(id)
