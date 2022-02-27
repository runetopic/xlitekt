package com.runetopic.xlitekt.plugin.script.packet.disassembler.handler

import com.runetopic.xlitekt.network.packet.CloseModalPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacketHandler

/**
 * @author Jordan Abraham
 */
onPacketHandler<CloseModalPacket> {
    player.interfaces.closeModal()
}
