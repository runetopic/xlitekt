package script.block.player

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.render.Render.Hit
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeSmart

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<Hit>(1, 0x4) {
    val splatsNormal = buildPacket {
        splats.forEach {
            writeSmart(it.type::id)
            writeSmart(it::damage)
            writeSmart(it::delay)
        }
    }.readBytes()

    val splatsTinted = buildPacket {
        splats.forEach {
            val type = it.type
            val interacting = it.isInteracting(actor, it.source)
            writeSmart { if (!interacting) type.id + 1 else type.id }
            writeSmart(it::damage)
            writeSmart(it::delay)
        }
    }.readBytes()

    val barsNormal = buildPacket {
        bars.forEach {
            writeSmart(it::id)
            writeSmart { 0 }
            writeSmart { 0 }
            writeByteAdd { it.percentage(actor) }
        }
    }.readBytes()

    buildPacket {
        // Normal block.
        writeByte(splats::size)
        writeBytes { splatsNormal }
        writeByteNegate(bars::size)
        writeBytes { barsNormal }
        // Tinted block.
        writeByte(splats::size)
        writeBytes { splatsTinted }
        writeByteNegate(bars::size)
        writeBytes { barsNormal }
    }
}
