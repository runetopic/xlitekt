package xlitekt.plugin.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.packedToChildId
import xlitekt.shared.packedToInterfaceId

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

onPacketHandler<OpHeldPacket> {
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
