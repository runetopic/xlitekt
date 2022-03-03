package xlitekt.cache.provider.config.spotanimation

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider
import java.lang.IllegalArgumentException

/**
 * @author Jordan Abraham
 */
class SpotAnimationEntryTypeProvider : EntryTypeProvider<SpotAnimationEntryType>() {

    override fun load(): Map<Int, SpotAnimationEntryType> = store
        .index(CONFIG_INDEX)
        .group(SPOT_ANIMATION_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(SpotAnimationEntryType(it.id)) }
        .associateBy(SpotAnimationEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: SpotAnimationEntryType): SpotAnimationEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.archive = readUShort().toInt()
            2 -> type.sequence = readUShort().toInt()
            4 -> type.widthScale = readUShort().toInt()
            5 -> type.heightScale = readUShort().toInt()
            6 -> type.orientation = readUShort().toInt()
            7 -> type.ambient = readUByte().toInt()
            8 -> type.contrast = readUByte().toInt()
            40 -> repeat(readUByte().toInt()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte().toInt()) {
                discard(4) // Discard retexture.
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
