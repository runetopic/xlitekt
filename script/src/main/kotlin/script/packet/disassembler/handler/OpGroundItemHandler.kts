import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.packet.OpGroundItemPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler

/**
 * @author Justin Kenney
 */
private val logger = InlineLogger()

onPacketHandler<OpGroundItemPacket> {
    logger.debug { packet }
}
