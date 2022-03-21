package script.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.OverheadChat
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<OverheadChat>(3, 0x10) {
    buildPacket {
        writeStringCp1252NullTerminated(text)
    }
}
