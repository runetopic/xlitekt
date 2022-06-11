package script.block.player

import com.runetopic.cryptography.toHuffman
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeShort
import xlitekt.cache.provider.binary.huffman.HuffmanEntryTypeProvider
import xlitekt.game.actor.render.Render.PublicChat
import xlitekt.game.actor.render.block.dynamicPlayerUpdateBlock
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.formatChatMessage
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
val provider by inject<HuffmanEntryTypeProvider>()

dynamicPlayerUpdateBlock<PublicChat>(index = 7, mask = 0x20, size = -1) {
    it.writeShort(packedEffects.toShort())
    it.writeByteNegate(rights)
    it.writeByteAdd(0) // Auto chat
    val bytes = ByteArray(256)
    val formatted = message.formatChatMessage()
    formatted.toHuffman(provider.entries().first().huffman!!, bytes).also { size ->
        it.writeByte((size + 1).toByte())
        it.writeSmart(formatted.length)
        it.writeFully(bytes, 0, size)
    }
}
