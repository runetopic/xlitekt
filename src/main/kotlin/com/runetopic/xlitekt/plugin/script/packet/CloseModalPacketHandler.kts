package com.runetopic.xlitekt.plugin.script.packet

import com.runetopic.xlitekt.network.packet.CloseModalPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacket

/**
 * @author Jordan Abraham
 */
onPacket<CloseModalPacket> {
    player.interfaceManager.closeModal()
}
