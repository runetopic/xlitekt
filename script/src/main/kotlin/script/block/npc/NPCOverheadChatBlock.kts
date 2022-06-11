package script.block.npc

import xlitekt.game.actor.render.Render.OverheadChat
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<OverheadChat>(3, 0x10) {
    buildFixedPacket(text.length + 1) {
        writeStringCp1252NullTerminated(text)
    }
}
