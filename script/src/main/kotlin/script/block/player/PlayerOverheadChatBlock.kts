package script.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.OverheadChat
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<OverheadChat>(13, 0x8) {
    buildPacket {
        writeStringCp1252NullTerminated { text }
    }
}
