package xlitekt.cache.provider.config.underlay

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUMedium

/**
 * @author Jordan Abraham
 */
class FloorUnderlayEntryTypeProvider : EntryTypeProvider<FloorUnderlayEntryType>() {

    override fun load(): Map<Int, FloorUnderlayEntryType> = store
        .index(CONFIG_INDEX)
        .group(FLOOR_UNDERLAY_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(FloorUnderlayEntryType(it.id)) }
        .associateBy(FloorUnderlayEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: FloorUnderlayEntryType): FloorUnderlayEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.rgb = readUMedium()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
