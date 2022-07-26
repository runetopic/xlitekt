package script.packet.disassembler.handler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.content.ui.UserInterfaceEvent
import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.insert
import xlitekt.shared.packedToChildId
import xlitekt.shared.packedToInterfaceId

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

insert<PacketHandlerListener>().handlePacket<OpHeldPacket> {
    val index = packet.index
    val fromInterfaceId = packet.fromPackedInterface.packedToInterfaceId()
    val fromChildId = packet.fromPackedInterface.packedToChildId()
    val fromSlotId = packet.fromSlotId
    val fromItemId = packet.fromItemId

    val toInterfaceId = packet.toPackedInterface.packedToInterfaceId()
    val toChildId = packet.toPackedInterface.packedToChildId()
    val toSlotId = packet.toSlotId
    val toItemId = packet.toItemId

    val event = UserInterfaceEvent.OpHeldEvent(
        index = index,
        fromInterfaceId = fromInterfaceId,
        fromChildId = fromChildId,
        fromSlotId = fromSlotId,
        fromItemId = fromItemId,
        toInterfaceId = toInterfaceId,
        toChildId = toChildId,
        toSlotId = toSlotId,
        toItemId = toItemId,
    )

    val listener = player.interfaces.listeners.find { it.userInterface.interfaceInfo.id == fromInterfaceId } ?: return@handlePacket
    listener.opHeld(event)
    logger.debug { "Clicked op held fromInterfaceId=$fromInterfaceId, toInterfaceId=$toInterfaceId, fromChildId=$fromChildId, toChildId=$toChildId, fromSlotId=$fromSlotId, toSlotId=$toSlotId, fromItemId=$fromItemId, toItemId=$toItemId, index=$index" }
}
