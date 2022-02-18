package com.runetopic.xlitekt.plugin.script.packet

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.network.packet.OpHeldPacket
import com.runetopic.xlitekt.network.packet.disassembler.handler.onPacket
import com.runetopic.xlitekt.shared.packedToChildId
import com.runetopic.xlitekt.shared.packedToInterfaceId

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

onPacket<OpHeldPacket> {
    val index = packet.index
    val fromInterfaceId = packet.fromPackedInterface.packedToInterfaceId()
    val fromChildId = packet.fromPackedInterface.packedToChildId()
    val fromSlotId = packet.fromSlotId
    val fromItemId = packet.fromItemId

    val toInterfaceId = packet.toPackedInterface.packedToInterfaceId()
    val toChildId = packet.toPackedInterface.packedToChildId()
    val toSlotId = packet.toSlotId
    val toItemId = packet.toItemId

    logger.debug { "Clicked op held fromInterfaceId=$fromInterfaceId, toInterfaceId=$toInterfaceId, fromChildId=$fromChildId, toChildId=$toChildId, fromSlotId=$fromSlotId, toSlotId=$toSlotId, fromItemId=$fromItemId, toItemId=$toItemId, index=$index" }
}
