package xlitekt.cache.provider.config.underlay

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUMedium
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class FloorUnderlayEntryTypeProvider : EntryTypeProvider<FloorUnderlayEntryType>() {

    override fun load(): Map<Int, FloorUnderlayEntryType> = store
        .index(CONFIG_INDEX)
        .group(FLOOR_UNDERLAY_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(FloorUnderlayEntryType(it.id)) }
        .associateBy(FloorUnderlayEntryType::id)

    override fun ByteBuffer.loadEntryType(type: FloorUnderlayEntryType): FloorUnderlayEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.rgb = readUMedium()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
