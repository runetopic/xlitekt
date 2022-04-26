package script.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.Hit
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeSmart

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<Hit>(2, 0x1) {
    buildPacket {
        writeByteAdd(hits::size)
        hits.forEach {
            val type = it.type
            val interacting = it.isInteracting(actor, it.source)
            val tinted = type.isTinted()
            writeSmart { if (!interacting && tinted) type.id + 1 else type.id }
            writeSmart(it::damage)
            writeSmart(it::delay)
        }

        writeByteNegate(bars::size)
        bars.forEach {
            writeSmart(it::id)
            writeSmart { 0 } // ?
            writeSmart { 0 } // ?
            writeByteAdd { it.percentage(actor) }
        }
    }
}
