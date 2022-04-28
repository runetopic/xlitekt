package xlitekt.cache.provider.binary.title

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class TitleEntryTypeProvider : EntryTypeProvider<TitleEntryType>() {

    override fun load(): Map<Int, TitleEntryType> = store
        .index(BINARY_INDEX)
        .group("title.jpg")
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(TitleEntryType(it.id)) }
        .associateBy(TitleEntryType::id)

    override fun ByteReadPacket.loadEntryType(type: TitleEntryType): TitleEntryType {
        type.pixels = readBytes()
        assertEmptyAndRelease()
        return type
    }
}
