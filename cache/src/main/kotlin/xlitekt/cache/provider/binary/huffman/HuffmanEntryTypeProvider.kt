package xlitekt.cache.provider.binary.huffman

import com.runetopic.cryptography.huffman.Huffman
import io.ktor.util.moveToByteArray
import xlitekt.cache.provider.EntryTypeProvider
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class HuffmanEntryTypeProvider : EntryTypeProvider<HuffmanEntryType>() {

    override fun load(): Map<Int, HuffmanEntryType> {
        val file = store.index(BINARY_INDEX).group("huffman").file(0)
        return mapOf(file.id to ByteBuffer.wrap(file.data).loadEntryType(HuffmanEntryType(file.id)))
    }

    override fun ByteBuffer.loadEntryType(type: HuffmanEntryType): HuffmanEntryType {
        type.huffman = Huffman(moveToByteArray())
        assertEmptyAndRelease()
        return type
    }
}
