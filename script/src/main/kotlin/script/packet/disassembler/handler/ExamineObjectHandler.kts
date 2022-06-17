package script.packet.disassembler.handler

import xlitekt.cache.provider.config.loc.LocEntryTypeProvider
import xlitekt.game.actor.player.message
import xlitekt.game.packet.ExamineObjectPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.inject
import xlitekt.shared.lazyInject
import xlitekt.shared.resource.ObjectExamines

private val provider by inject<LocEntryTypeProvider>()
private val objectExamines by inject<ObjectExamines>()

/**
 * @author Justin Kenney
 */
lazyInject<PacketHandlerListener>().handlePacket<ExamineObjectPacket> {
    if (!provider.exists(packet.objectID)) return@handlePacket
    val examine = objectExamines[packet.objectID] ?: return@handlePacket
    if (examine.message.isEmpty()) return@handlePacket
    player.message(examine::message)
}
