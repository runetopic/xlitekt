package com.runetopic.xlitekt.game.actor.render.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.actor.render.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeByteAdd
import com.runetopic.xlitekt.shared.toByte
import io.ktor.utils.io.core.buildPacket

class PlayerMovementTypeBlock : RenderingBlock<Player, Render.MovementType>(9, 0x400) {
    override fun build(actor: Player, render: Render.MovementType) = buildPacket {
        writeByteAdd((render.running.toByte() + 1).toByte())
    }
}
