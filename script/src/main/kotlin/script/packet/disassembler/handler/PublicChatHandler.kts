package script.packet.disassembler.handler

import com.runetopic.cryptography.fromHuffman
import xlitekt.cache.provider.binary.huffman.HuffmanEntryTypeProvider
import xlitekt.game.packet.PublicChatPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.inject

private val provider by inject<HuffmanEntryTypeProvider>()

onPacketHandler<PublicChatPacket> {
    val decompressedString = packet.data.fromHuffman(provider.entryType(0)?.huffman!!, this.packet.length)
    player.publicChat(decompressedString, packedEffects = packet.color shl 8 or packet.effect, player.rights)
}
