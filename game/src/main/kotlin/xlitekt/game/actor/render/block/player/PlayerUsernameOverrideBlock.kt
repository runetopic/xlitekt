package xlitekt.game.actor.render.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Tyler Telis
 */
class PlayerUsernameOverrideBlock : RenderingBlock<Player, Render.UsernameOverride>(6, 0x100) {
    override fun build(actor: Player, render: Render.UsernameOverride) = buildPacket {
        writeStringCp1252NullTerminated(render.prefix)
        writeStringCp1252NullTerminated(render.infix)
        writeStringCp1252NullTerminated(render.suffix)
    }
}
