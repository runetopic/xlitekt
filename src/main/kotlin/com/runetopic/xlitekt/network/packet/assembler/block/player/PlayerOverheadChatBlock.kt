package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class PlayerOverheadChatBlock : RenderingBlock<Player, Render.OverheadChat>(13, 0x8) {
    override fun build(actor: Player, render: Render.OverheadChat) = buildPacket {
        writeStringCp1252NullTerminated(render.text)
    }
}
