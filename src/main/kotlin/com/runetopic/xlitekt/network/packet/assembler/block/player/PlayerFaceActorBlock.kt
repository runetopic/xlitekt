package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class PlayerFaceActorBlock : RenderingBlock<Player, Render.FaceActor>(4, 0x80) {
    override fun build(actor: Player, render: Render.FaceActor) = buildPacket {
        writeShortLittleEndianAdd(render.index.toShort())
    }
}
