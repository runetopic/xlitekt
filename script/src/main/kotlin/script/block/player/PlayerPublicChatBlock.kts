package script.block.player

import com.runetopic.cryptography.toHuffman
import xlitekt.cache.provider.binary.huffman.HuffmanEntryTypeProvider
import xlitekt.game.actor.render.Render.PublicChat
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.allocateDynamic
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.formatChatMessage
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
val provider by inject<HuffmanEntryTypeProvider>()

onPlayerUpdateBlock<PublicChat>(7, 0x20) {
    allocateDynamic(256) {
        writeShort { packedEffects }
        writeByteNegate { rights }
        writeByteAdd { 0 } // Auto chat
        val bytes = ByteArray(256)
        val formatted = message.formatChatMessage()
        formatted.toHuffman(provider.entries().first().huffman!!, bytes).also {
            writeByte { it + 1 }
            writeSmart(formatted::length)
            put(bytes, 0, it)
        }
    }
}
