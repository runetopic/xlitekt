package xlitekt.cache.provider.config.spotanimation

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class SpotAnimationEntryTypeProvider : EntryTypeProvider<SpotAnimationEntryType>() {

    override fun load(): Map<Int, SpotAnimationEntryType> = store
        .index(CONFIG_INDEX)
        .group(SPOT_ANIMATION_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(SpotAnimationEntryType(it.id)) }
        .associateBy(SpotAnimationEntryType::id)

    override fun ByteBuffer.loadEntryType(type: SpotAnimationEntryType): SpotAnimationEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.archive = readUShort()
            2 -> type.sequence = readUShort()
            4 -> type.widthScale = readUShort()
            5 -> type.heightScale = readUShort()
            6 -> type.orientation = readUShort()
            7 -> type.ambient = readUByte()
            8 -> type.contrast = readUByte()
            40 -> repeat(readUByte()) {
                discard(4) // Discard recolor.
            }
            41 -> repeat(readUByte()) {
                discard(4) // Discard retexture.
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
