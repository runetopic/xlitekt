package com.runetopic.xlitekt.plugin.script.packet

import com.runetopic.xlitekt.game.ui.InterfaceLayout
import com.runetopic.xlitekt.network.packet.WindowStatusPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacket

/**
 * @author Jordan Abraham
 */
onPacket<WindowStatusPacket> {
    val interfaceLayout = InterfaceLayout.values().find { it.id == packet.displayMode }
        ?: throw IllegalStateException("Unhandled display mode sent from client. Mode=${packet.displayMode} Width=${packet.width} Height=${packet.height}")
    if (player.interfaceManager.currentInterfaceLayout == interfaceLayout) return@onPacket
    player.interfaceManager.switchLayout(interfaceLayout)
}
