package xlitekt.cache.provider.config.overlay

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUMedium

/**
 * @author Jordan Abraham
 */
class FloorOverlayEntryTypeProvider : EntryTypeProvider<FloorOverlayEntryType>() {

    override fun load(): Map<Int, FloorOverlayEntryType> = store
        .index(CONFIG_INDEX)
        .group(FLOOR_OVERLAY_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(FloorOverlayEntryType(it.id)) }
        .associateBy(FloorOverlayEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: FloorOverlayEntryType): FloorOverlayEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> type.primaryRgb = readUMedium()
            2 -> type.texture = readUByte().toInt()
            5 -> type.hideUnderlay = false
            7 -> type.secondaryRgb = readUMedium()
            8 -> {
                // Unused.
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
