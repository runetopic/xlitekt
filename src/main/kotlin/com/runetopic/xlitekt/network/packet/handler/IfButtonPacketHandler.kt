package com.runetopic.xlitekt.network.packet.handler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.cache.Cache.entryType
import com.runetopic.xlitekt.cache.provider.ui.InterfaceEntryType
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.ui.InterfaceMapping.interfaceListener
import com.runetopic.xlitekt.game.ui.InterfaceMapping.interfaceListeners
import com.runetopic.xlitekt.game.ui.InterfaceMapping.userInterfaces
import com.runetopic.xlitekt.game.ui.UserInterfaceEvent
import com.runetopic.xlitekt.network.packet.IfButtonPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
class IfButtonPacketHandler : PacketHandler<IfButtonPacket> {

    private val logger = InlineLogger()

    override suspend fun handlePacket(player: Player, packet: IfButtonPacket) {
        val index = packet.index
        val interfaceId = packet.packedInterface shr 16
        val childId = packet.packedInterface and 0xffff
        val slotId = packet.slotId
        val itemId = packet.itemId
        val entry = entryType<InterfaceEntryType>(packet.packedInterface)
        val clickEvent = UserInterfaceEvent.ButtonClickEvent(
            player = player,
            index = index,
            interfaceId = interfaceId,
            childId = childId,
            option = childId,
            slotId = slotId,
            itemId = itemId,
            action = entry?.actions?.firstOrNull() ?: "*"
        )
        val rsInterface = userInterfaces[interfaceId] ?: return logger.debug { "User interface is not registered. Event = $clickEvent" }
        interfaceListener(rsInterface)?.click(clickEvent) ?: return logger.debug { "User interface does not have an associated listener. Event = $clickEvent" }
    }
}
