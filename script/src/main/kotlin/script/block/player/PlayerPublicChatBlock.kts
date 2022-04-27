package script.block.player

import com.runetopic.cryptography.huffman.Huffman
import com.runetopic.cryptography.toHuffman
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import xlitekt.game.actor.render.Render.PublicChat
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
val huffman by inject<Huffman>()

onPlayerUpdateBlock<PublicChat>(7, 0x20) {
    buildPacket {
        writeShort { packedEffects }
        writeByteNegate { rights }
        writeByteAdd { 0 } // Auto chat
        val compressed = ByteArray(256)
        val compressedLength = message.toHuffman(huffman, compressed)
        writeByte { compressedLength + 1 }
        writeSmart(message::length)
        writeFully(compressed, 0, compressedLength)
    }
}
