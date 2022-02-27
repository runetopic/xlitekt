package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.toByte

class PlayerMovementTypeBlock : RenderingBlock<Player, Render.MovementType>(9, 0x400) {
    override fun build(actor: Player, render: Render.MovementType) = buildPacket {
        writeByteAdd((render.running.toByte() + 1).toByte())
    }
}
