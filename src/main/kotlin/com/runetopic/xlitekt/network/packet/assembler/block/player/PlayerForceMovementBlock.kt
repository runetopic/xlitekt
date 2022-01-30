package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeByteNegate
import com.runetopic.xlitekt.util.ext.writeByteSubtract
import com.runetopic.xlitekt.util.ext.writeShortAdd
import com.runetopic.xlitekt.util.ext.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class PlayerForceMovementBlock : RenderingBlock<Player, Render.ForceMovement>(3, 0x4000) {
    override fun build(actor: Player, render: Render.ForceMovement) = buildPacket {
        writeByteNegate((render.firstTile.x - actor.tile.x).toByte())
        writeByteNegate((render.firstTile.z - actor.tile.z).toByte())
        writeByteSubtract((render.secondTile?.x?.minus(actor.tile.x) ?: 0).toByte())
        writeByteSubtract((render.secondTile?.z?.minus(actor.tile.z) ?: 0).toByte())
        writeShortLittleEndianAdd((render.firstDelay * 30).toShort())
        writeShortLittleEndianAdd((render.secondDelay * 30).toShort())
        writeShortAdd(render.rotation.toShort())
    }
}
