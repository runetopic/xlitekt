package com.runetopic.xlitekt.game.actor.render.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.actor.render.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeByteNegate
import com.runetopic.xlitekt.shared.buffer.writeByteSubtract
import com.runetopic.xlitekt.shared.buffer.writeShortAdd
import com.runetopic.xlitekt.shared.buffer.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket

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
