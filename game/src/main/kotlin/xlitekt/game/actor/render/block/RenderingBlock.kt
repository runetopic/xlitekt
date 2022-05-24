package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
data class RenderingBlock(
    val index: Int,
    val mask: Int,
    val packet: Render.() -> ByteReadPacket
)

/**
 * Creates a new buffer from a collection of HighDefinitionRenderingBlock's to be consumed by the player info packet.
 */
internal fun Collection<HighDefinitionRenderingBlock>.createHighDefinitionUpdatesBuffer(player: Player): ByteArray? {
    if (isEmpty()) return null
    val packet = buildPacket {
        writeMask(this@createHighDefinitionUpdatesBuffer.fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x10 else it })
        for (update in this@createHighDefinitionUpdatesBuffer) {
            val packet = update.renderingBlock.packet.invoke(update.render)
            val bytes = packet.readBytes()
            packet.release()
            player.addLowDefinitionRenderingBlock(update, bytes)
            writeBytes { bytes }
        }
    }
    val bytes = packet.readBytes()
    packet.release()
    return bytes
}

/**
 * Creates a new buffer from a collection of LowDefinitionRenderingBlock's to be consumed by the player info packet.
 */
internal fun Collection<LowDefinitionRenderingBlock>.createLowDefinitionUpdatesBuffer(): ByteArray? {
    if (isEmpty()) return null
    val packet = buildPacket {
        writeMask(this@createLowDefinitionUpdatesBuffer.fold(0) { current, next -> current or next.mask }.let { if (it > 0xff) it or 0x10 else it })
        for (update in this@createLowDefinitionUpdatesBuffer) {
            writeBytes(update::block)
        }
    }
    val bytes = packet.readBytes()
    packet.release()
    return bytes
}

fun BytePacketBuilder.buildNPCUpdateBlocks(npc: NPC) = with(npc.highDefinitionRenderingBlocks()) {
    writeMask(fold(0) { current, next -> current or next.renderingBlock.mask }.let { if (it > 0xff) it or 0x4 else it })
    for (update in this) {
        writeBytes(update.renderingBlock.packet.invoke(update.render)::readBytes)
    }
}

private fun BytePacketBuilder.writeMask(mask: Int) {
    if (mask > 0xff) writeShortLittleEndian { mask } else writeByte { mask }
}
