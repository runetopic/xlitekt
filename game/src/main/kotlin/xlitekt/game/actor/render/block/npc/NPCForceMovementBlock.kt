package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Tyler Telis
 */
class NPCForceMovementBlock : RenderingBlock<NPC, Render.ForceMovement>(7, 0x400) {
    override fun build(actor: NPC, render: Render.ForceMovement) = buildPacket {
        writeByteNegate((render.firstLocation.x - actor.location.x).toByte())
        writeByte((render.firstLocation.z - actor.location.z).toByte())
        writeByteNegate((render.secondLocation?.x?.minus(actor.location.x) ?: 0).toByte())
        writeByte((render.secondLocation?.z?.minus(actor.location.z) ?: 0).toByte())
        writeShortLittleEndianAdd((render.firstDelay * 30).toShort())
        writeShort((render.secondDelay * 30).toShort())
        writeShortLittleEndianAdd(render.rotation.toShort())
    }
}
