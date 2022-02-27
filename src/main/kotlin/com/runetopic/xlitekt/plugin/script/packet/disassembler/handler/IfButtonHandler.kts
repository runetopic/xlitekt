package com.runetopic.xlitekt.plugin.script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.cache.entryType
import com.runetopic.xlitekt.cache.provider.ui.InterfaceEntryType
import com.runetopic.xlitekt.game.ui.UserInterfaceEvent
import com.runetopic.xlitekt.network.packet.IfButtonPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacketHandler
import com.runetopic.xlitekt.shared.packedToChildId
import com.runetopic.xlitekt.shared.packedToInterfaceId

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
