package xlitekt.cache.provider.binary.huffman

import com.runetopic.cryptography.huffman.Huffman
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.cache.provider.EntryTypeProvider

/**
 * @author Jordan Abraham
 */
class HuffmanEntryTypeProvider : EntryTypeProvider<HuffmanEntryType>() {

    override fun load(): Map<Int, HuffmanEntryType> {
        val file = store.index(BINARY_INDEX).group("huffman").file(0)
        return mapOf(file.id to ByteReadPacket(file.data).loadEntryType(HuffmanEntryType(file.id)))
    }

    override fun ByteReadPacket.loadEntryType(type: HuffmanEntryType): HuffmanEntryType {
        type.huffman = Huffman(readBytes())
        assertEmptyAndRelease()
        return type
    }
}
