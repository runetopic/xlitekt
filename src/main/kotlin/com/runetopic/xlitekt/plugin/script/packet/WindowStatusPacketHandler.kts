package com.runetopic.xlitekt.plugin.script.packet

import com.runetopic.xlitekt.game.ui.InterfaceLayout
import com.runetopic.xlitekt.game.vars.VarBit
import com.runetopic.xlitekt.network.packet.WindowStatusPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacket

/**
 * @author Jordan Abraham
 */
onPacket<WindowStatusPacket> {
    var interfaceLayout = InterfaceLayout.values().find { it.id == packet.displayMode }
        ?: throw IllegalStateException("Unhandled display mode sent from client. Mode=${packet.displayMode} Width=${packet.width} Height=${packet.height}")
    if (interfaceLayout == InterfaceLayout.RESIZABLE && player.vars.value(VarBit.SideStonesArrangement) == 1) {
        interfaceLayout = InterfaceLayout.RESIZABLE_MODERN
    }
    if (player.interfaces.currentInterfaceLayout == interfaceLayout) return@onPacket
    player.interfaces.switchLayout(interfaceLayout)
}
