package com.runetopic.xlitekt.plugin.script.packet

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.cache.Cache
import com.runetopic.xlitekt.cache.provider.ui.InterfaceEntryType
import com.runetopic.xlitekt.game.ui.UserInterfaceEvent
import com.runetopic.xlitekt.network.packet.IfButtonPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacket

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

onPacket<IfButtonPacket> {
    val index = packet.index
    val interfaceId = packet.packedInterface shr 16
    val childId = packet.packedInterface and 0xffff
    val slotId = packet.slotId
    val itemId = packet.itemId
    val entry = Cache.entryType<InterfaceEntryType>(packet.packedInterface)
    val clickEvent = UserInterfaceEvent.ButtonClickEvent(
        index = index,
        interfaceId = interfaceId,
        childId = childId,
        option = childId,
        slotId = slotId,
        itemId = itemId,
        action = entry?.actions?.firstOrNull() ?: "*"
    )
    val listener = player.interfaceManager.listeners.find { it.userInterface.interfaceInfo.id == interfaceId }
    listener?.click(clickEvent) ?: logger.debug { "User interface does not have an associated listener. Event = $clickEvent" }
}
