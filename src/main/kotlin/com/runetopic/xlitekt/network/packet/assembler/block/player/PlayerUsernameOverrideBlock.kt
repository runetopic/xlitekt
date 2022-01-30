package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.buildPacket

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
