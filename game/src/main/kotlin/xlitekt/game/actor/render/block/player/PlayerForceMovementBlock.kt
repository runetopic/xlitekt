package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Tyler Telis
 */
class PlayerForceMovementBlock : RenderingBlock<Player, Render.ForceMovement>(3, 0x4000) {
    override fun build(actor: Player, render: Render.ForceMovement) = buildPacket {
        writeByteNegate((render.firstLocation.x - actor.location.x).toByte())
        writeByteNegate((render.firstLocation.z - actor.location.z).toByte())
        writeByteSubtract((render.secondLocation?.x?.minus(actor.location.x) ?: 0).toByte())
        writeByteSubtract((render.secondLocation?.z?.minus(actor.location.z) ?: 0).toByte())
        writeShortLittleEndianAdd((render.firstDelay * 30).toShort())
        writeShortLittleEndianAdd((render.secondDelay * 30).toShort())
        writeShortAdd(render.rotation.toShort())
    }
}
