package com.runetopic.xlitekt.game.actor.render.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.actor.render.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeIntV2
import com.runetopic.xlitekt.shared.buffer.writeShortAdd
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class PlayerSpotAnimationBlock : RenderingBlock<Player, Render.SpotAnimation>(10, 0x800) {
    override fun build(actor: Player, render: Render.SpotAnimation) = buildPacket {
        writeShortAdd(render.id.toShort())
        writeIntV2(render.packedMetaData())
    }
}
