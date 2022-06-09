package script.block.player

import xlitekt.game.actor.render.Render.Hit
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.allocateDynamic
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeSmart

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<Hit>(1, 0x4) {
    val splatsNormal = allocateDynamic(splats.size * 6) {
        splats.forEach {
            writeSmart(it.type.id)
            writeSmart(it.damage)
            writeSmart(it.delay)
        }
    }

    val splatsTinted = allocateDynamic(splats.size * 6) {
        splats.forEach {
            val type = it.type
            val interacting = it.isInteracting(actor, it.source)
            writeSmart(if (!interacting) type.id + 1 else type.id)
            writeSmart(it.damage)
            writeSmart(it.delay)
        }
    }

    val barsNormal = allocateDynamic(bars.size * 7) {
        bars.forEach {
            writeSmart(it.id)
            writeSmart(0)
            writeSmart(0)
            writeByteAdd(it.percentage(actor))
        }
    }

    allocate(splatsNormal.size + splatsTinted.size + (barsNormal.size * 2) + 4) {
        // Normal block.
        writeByte(splats.size)
        writeBytes(splatsNormal)
        writeByteNegate(bars.size)
        writeBytes(barsNormal)
        // Tinted block.
        writeByte(splats.size)
        writeBytes(splatsTinted)
        writeByteNegate(bars.size)
        writeBytes(barsNormal)
    }
}
