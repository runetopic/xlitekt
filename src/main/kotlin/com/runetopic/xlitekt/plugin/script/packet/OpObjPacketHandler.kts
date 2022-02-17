package com.runetopic.xlitekt.plugin.script.packet

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.network.packet.OpObjPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacket

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

onPacket<OpObjPacket> {
    val index = packet.index
    val packedInterface = packet.packedInterface
    val interfaceId = packedInterface shr 16
    val childId = packedInterface and 0xffff
    val slotId = packet.slotId
    val itemId = packet.itemId
    logger.debug { "Clicked obj op interfaceId=$interfaceId, childId=$childId, slotId=$slotId, itemId=$itemId, index=$index" }
}
