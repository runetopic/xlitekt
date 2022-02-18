package com.runetopic.xlitekt.plugin.script.packet

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.network.packet.OpObjPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacket
import com.runetopic.xlitekt.shared.packedToChildId
import com.runetopic.xlitekt.shared.packedToInterfaceId

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

onPacket<OpObjPacket> {
    val index = packet.index
    val interfaceId = packet.packedInterface.packedToInterfaceId()
    val childId = packet.packedInterface.packedToChildId()
    val slotId = packet.slotId
    val itemId = packet.itemId
    logger.debug { "Clicked obj op interfaceId=$interfaceId, childId=$childId, slotId=$slotId, itemId=$itemId, index=$index" }
}
