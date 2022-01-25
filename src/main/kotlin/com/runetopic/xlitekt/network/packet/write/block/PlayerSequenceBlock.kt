package com.runetopic.xlitekt.network.packet.write.block

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket

class PlayerSequenceBlock : RenderingBlock<Player, Render.Animation>(5, 0x1) {

    override fun build(actor: Player, render: Render.Animation): ByteReadPacket = buildPacket {
    }
}
