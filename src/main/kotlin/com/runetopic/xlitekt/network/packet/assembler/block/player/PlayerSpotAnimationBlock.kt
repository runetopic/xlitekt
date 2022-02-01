package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeIntV2
import com.runetopic.xlitekt.util.ext.writeShortAdd
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
