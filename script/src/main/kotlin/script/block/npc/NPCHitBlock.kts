package script.block.npc

import xlitekt.game.actor.render.Render.Hit
import xlitekt.game.actor.render.block.NPCRenderingBlockListener
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<NPCRenderingBlockListener>().dynamicNpcUpdateBlock<Hit>(index = 2, mask = 0x1, size = -1) {
    it.writeByteAdd(splats.size)
    for (splat in splats) {
        val type = splat.type
        val interacting = splat.isInteracting(actor, splat.source)
        it.writeSmart(if (!interacting) type.id + 1 else type.id)
        it.writeSmart(splat.damage)
        it.writeSmart(splat.delay)
    }

    it.writeByteNegate(bars.size)
    for (bar in bars) {
        it.writeSmart(bar.id)
        it.writeSmart(0)
        it.writeSmart(0)
        it.writeByteAdd(bar.percentage(actor))
    }
}
