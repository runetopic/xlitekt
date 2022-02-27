package xlitekt.plugin.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.packet.OpObjPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler
import xlitekt.shared.packedToChildId
import xlitekt.shared.packedToInterfaceId

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

onPacketHandler<OpObjPacket> {
    val index = packet.index
    val interfaceId = packet.packedInterface.packedToInterfaceId()
    val childId = packet.packedInterface.packedToChildId()
    val slotId = packet.slotId
    val itemId = packet.itemId
    logger.debug { "Clicked obj op interfaceId=$interfaceId, childId=$childId, slotId=$slotId, itemId=$itemId, index=$index" }
}
