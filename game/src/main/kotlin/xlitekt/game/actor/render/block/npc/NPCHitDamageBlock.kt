package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeSmart

/**
 * @author Tyler Telis
 */
class NPCHitDamageBlock : RenderingBlock<NPC, Render.HitDamage>(2, 0x1) {
    override fun build(actor: NPC, render: Render.HitDamage) = buildPacket {
        writeByteAdd(actor.nextHits.size.toByte())

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
