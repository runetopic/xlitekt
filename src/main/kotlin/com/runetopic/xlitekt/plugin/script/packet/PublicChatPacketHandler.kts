package com.runetopic.xlitekt.plugin.script.packet

import com.runetopic.cryptography.fromHuffman
import com.runetopic.cryptography.huffman.Huffman
import com.runetopic.xlitekt.network.packet.PublicChatPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacket
import com.runetopic.xlitekt.plugin.koin.inject

private val huffman by inject<Huffman>()

onPacket<PublicChatPacket> {
    val decompressedString = packet.data.fromHuffman(huffman, this.packet.length)
    player.publicChat(decompressedString, packedEffects = packet.effect)
}
