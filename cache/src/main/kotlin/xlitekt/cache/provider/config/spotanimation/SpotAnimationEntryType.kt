package xlitekt.cache.provider.config.spotanimation

import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class SpotAnimationEntryType(
    override val id: Int,
    var sequence: Int = -1,
    var widthScale: Int = 128,
    var heightScale: Int = 128,
    var orientation: Int = 0,
    var ambient: Int = 0,
    var contrast: Int = 0,
    var archive: Int = 0
) : EntryType(id)
