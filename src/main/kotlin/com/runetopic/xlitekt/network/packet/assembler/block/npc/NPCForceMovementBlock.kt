package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeByteNegate
import com.runetopic.xlitekt.util.ext.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

class NPCForceMovementBlock : RenderingBlock<NPC, Render.ForceMovement>(7, 0x400) {
    override fun build(actor: NPC, render: Render.ForceMovement) = buildPacket {
        writeByteNegate((render.firstTile.x - actor.tile.x).toByte())
        writeByte((render.firstTile.z - actor.tile.z).toByte())
        writeByteNegate((render.secondTile?.x?.minus(actor.tile.x) ?: 0).toByte())
        writeByte((render.secondTile?.z?.minus(actor.tile.z) ?: 0).toByte())
        writeShortLittleEndianAdd((render.firstDelay * 30).toShort())
        writeShort((render.secondDelay * 30).toShort())
        writeShortLittleEndianAdd(render.rotation.toShort())
    }
}
