package script.packet.disassembler.handler

import xlitekt.cache.provider.config.obj.ObjEntryTypeProvider
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineItemPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.inject
import xlitekt.shared.lazyInject
import xlitekt.shared.resource.ItemExamines

private val itemExamines by inject<ItemExamines>()
private val provider by inject<ObjEntryTypeProvider>()

/**
 * @author Justin Kenney
 */
lazyInject<PacketHandlerListener>().handlePacket<ExamineItemPacket> {
    if (!provider.exists(packet.itemId)) return@handlePacket

    val examine = itemExamines[packet.itemId] ?: return@handlePacket
    if (examine.message.isEmpty()) return@handlePacket

    player.message(examine::message)
}
