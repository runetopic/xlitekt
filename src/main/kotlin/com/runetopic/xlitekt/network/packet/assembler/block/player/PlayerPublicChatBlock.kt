package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.cryptography.huffman.Huffman
import com.runetopic.cryptography.toHuffman
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.ext.writeByteAdd
import com.runetopic.xlitekt.util.ext.writeByteNegate
import com.runetopic.xlitekt.util.ext.writeSmart
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeFully
import io.ktor.utils.io.core.writeShort

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
        val length = render.message.toHuffman(huffman, compressed)
        writeByte((length + 1).toByte())
        writeSmart(render.message.length)
        writeFully(compressed, 0, length)
    }
}
