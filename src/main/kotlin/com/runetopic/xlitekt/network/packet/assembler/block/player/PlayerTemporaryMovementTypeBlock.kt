package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import io.ktor.utils.io.core.buildPacket

class PlayerTemporaryMovementTypeBlock : RenderingBlock<Player, Render.TemporaryMovementType>(2, 0x2000) {
    override fun build(actor: Player, render: Render.TemporaryMovementType) = buildPacket {
        writeByte(render.id.toByte())
    }
}
