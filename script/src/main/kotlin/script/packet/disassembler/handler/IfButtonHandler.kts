package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.cache.provider.ui.InterfaceEntryTypeProvider
import xlitekt.game.content.ui.UserInterfaceEvent
import xlitekt.game.packet.IfButtonPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.inject
import xlitekt.shared.lazyInject
import xlitekt.shared.packedToChildId
import xlitekt.shared.packedToInterfaceId

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()
private val interfaces by inject<InterfaceEntryTypeProvider>()

lazyInject<PacketHandlerListener>().handlePacket<IfButtonPacket> {
    val interfaceId = packet.packedInterface.packedToInterfaceId()
    val entry = interfaces.entryType(packet.packedInterface)
    val clickEvent = UserInterfaceEvent.ButtonClickEvent(
        index = packet.index,
        interfaceId = interfaceId,
        childId = packet.packedInterface.packedToChildId(),
        slotId = packet.slotId,
        itemId = packet.itemId,
        action = entry?.actions?.firstOrNull() ?: "*"
    )
    logger.debug { "Event = $clickEvent" }
    player.interfaces.listeners.find { it.userInterface.interfaceInfo.id == interfaceId }?.click(clickEvent)
}
