package script.packet.disassembler.handler

import com.runetopic.cryptography.fromHuffman
import xlitekt.cache.provider.binary.huffman.HuffmanEntryTypeProvider
import xlitekt.game.actor.chat
import xlitekt.game.packet.PublicChatPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.inject
import xlitekt.shared.insert

private val provider by inject<HuffmanEntryTypeProvider>()

insert<PacketHandlerListener>().handlePacket<PublicChatPacket> {
    player.chat(player.rights, packet.color shl 8 or packet.effect) { packet.data.fromHuffman(provider.entryType(0)?.huffman!!, packet.length) }
}
