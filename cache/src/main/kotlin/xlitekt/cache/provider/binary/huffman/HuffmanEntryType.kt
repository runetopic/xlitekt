package xlitekt.cache.provider.binary.huffman

import com.runetopic.cryptography.huffman.Huffman
import xlitekt.cache.provider.EntryType

/**
 * @author Jordan Abraham
 */
data class HuffmanEntryType(
    override val id: Int,
    var huffman: Huffman? = null
) : EntryType(id)
