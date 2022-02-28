package plugin.packet.disassembler.handler

import com.runetopic.cryptography.fromHuffman
import com.runetopic.cryptography.huffman.Huffman
import xlitekt.game.packet.PublicChatPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject

private val huffman by inject<Huffman>()

onPacketHandler<PublicChatPacket> {
    val decompressedString = packet.data.fromHuffman(huffman, this.packet.length)
    player.publicChat(decompressedString, packedEffects = packet.color shl 8 or packet.effect)
}
