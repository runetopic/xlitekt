package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Tyler Telis
 */
class PlayerOverheadChatBlock : RenderingBlock<Player, Render.OverheadChat>(13, 0x8) {
    override fun build(actor: Player, render: Render.OverheadChat) = buildPacket {
        writeStringCp1252NullTerminated(render.text)
    }
}
