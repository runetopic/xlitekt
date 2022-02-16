package com.runetopic.xlitekt.network.packet.assembler.block.player

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeByteAdd
import com.runetopic.xlitekt.shared.buffer.writeByteNegate
import com.runetopic.xlitekt.shared.buffer.writeSmart
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class PlayerHitDamageBlock : RenderingBlock<Player, Render.HitDamage>(1, 0x4) {

    override fun build(actor: Player, render: Render.HitDamage): ByteReadPacket = buildPacket {
        writeByte(actor.nextHits.size.toByte())

        actor.nextHits.forEach {
            val type = it.type
            val isInteracting = it.isInteracting(actor, it.source)
            val showTintedHitSplats = type.isTinted()

            writeSmart(if (!isInteracting && showTintedHitSplats) type.id + 1 else type.id)
            writeSmart(it.damage)
            writeSmart(it.delay)
        }

        writeByteNegate(actor.nextHitBars.size.toByte())

        actor.nextHitBars.forEach {
            writeSmart(it.id)
            writeSmart(0) // dunno yet
            writeSmart(0)
            writeByteAdd(it.percentage(actor).toByte())
        }
    }
}
