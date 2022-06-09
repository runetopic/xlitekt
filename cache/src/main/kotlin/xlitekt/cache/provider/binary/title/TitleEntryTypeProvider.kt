package xlitekt.cache.provider.binary.title

import io.ktor.util.moveToByteArray
import xlitekt.cache.provider.EntryTypeProvider
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class TitleEntryTypeProvider : EntryTypeProvider<TitleEntryType>() {

    override fun load(): Map<Int, TitleEntryType> = store
        .index(BINARY_INDEX)
        .group("title.jpg")
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(TitleEntryType(it.id)) }
        .associateBy(TitleEntryType::id)

    override fun ByteBuffer.loadEntryType(type: TitleEntryType): TitleEntryType {
        type.pixels = moveToByteArray()
        assertEmptyAndRelease()
        return type
    }
}
