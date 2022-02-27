package com.runetopic.xlitekt.game.actor.render.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.actor.render.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeByteNegate
import com.runetopic.xlitekt.shared.buffer.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

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
