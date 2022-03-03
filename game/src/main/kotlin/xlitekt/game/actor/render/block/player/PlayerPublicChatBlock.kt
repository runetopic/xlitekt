package xlitekt.game.actor.render.block.player

import com.runetopic.cryptography.huffman.Huffman
import com.runetopic.cryptography.toHuffman
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.inject

/**
 * @author Tyler Telis
 */
class PlayerPublicChatBlock : RenderingBlock<Player, Render.PublicChat>(7, 0x20) {
    private val huffman by inject<Huffman>()

    override fun build(actor: Player, render: Render.PublicChat) = buildPacket {
        writeShort(render.packedEffects.toShort())
        writeByteNegate(actor.rights.toByte())
        writeByteAdd(0) // Auto chat
        val compressed = ByteArray(256)
        val compressedLength = render.message.toHuffman(huffman, compressed)
        writeByte((compressedLength + 1).toByte())
        writeSmart(render.message.length)
        writeFully(compressed, 0, compressedLength)
    }
}
