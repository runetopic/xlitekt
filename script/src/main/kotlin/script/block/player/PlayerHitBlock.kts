package script.block.player

import xlitekt.game.actor.render.Render.Hit
import xlitekt.game.actor.render.block.PlayerRenderingBlockListener
import xlitekt.shared.buffer.dynamicBuffer
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PlayerRenderingBlockListener>().dynamicPlayerUpdateBlock<Hit>(index = 1, mask = 0x4, size = -1) {
    val splatsNormal = dynamicBuffer {
        for (splat in splats) {
            writeSmart(splat.type.id)
            writeSmart(splat.damage)
            writeSmart(splat.delay)
        }
    }

    val splatsTinted = dynamicBuffer {
        for (splat in splats) {
            val type = splat.type
            val interacting = splat.isInteracting(actor, splat.source)
            writeSmart(if (!interacting) type.id + 1 else type.id)
            writeSmart(splat.damage)
            writeSmart(splat.delay)
        }
    }

    val barsNormal = dynamicBuffer {
        for (bar in bars) {
            writeSmart(bar.id)
            writeSmart(0)
            writeSmart(0)
            writeByteAdd(bar.percentage(actor))
        }
    }

    // Normal block.
    it.writeByte(splats.size.toByte())
    it.writeBytes(splatsNormal)
    it.writeByteNegate(bars.size)
    it.writeBytes(barsNormal)
    // Tinted block.
    it.writeByte(splats.size.toByte())
    it.writeBytes(splatsTinted)
    it.writeByteNegate(bars.size)
    it.writeBytes(barsNormal)
}
