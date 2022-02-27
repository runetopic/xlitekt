package com.runetopic.xlitekt.game.actor.render.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.actor.render.block.RenderingBlock
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Tyler Telis
 */
class PlayerFaceDirectionBlock : RenderingBlock<Player, Render.FaceDirection>(12, 0x40) {
    override fun build(actor: Player, render: Render.FaceDirection) = buildPacket {
        writeShortLittleEndian(render.direction.toShort())
    }
}
