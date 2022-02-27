package xlitekt.plugin.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.cache.entryType
import xlitekt.cache.provider.ui.InterfaceEntryType
import xlitekt.game.packet.IfButtonPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.game.ui.UserInterfaceEvent
import xlitekt.shared.packedToChildId
import xlitekt.shared.packedToInterfaceId

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

onPacketHandler<IfButtonPacket> {
    val interfaceId = packet.packedInterface.packedToInterfaceId()
    val entry = entryType<InterfaceEntryType>(packet.packedInterface)
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
