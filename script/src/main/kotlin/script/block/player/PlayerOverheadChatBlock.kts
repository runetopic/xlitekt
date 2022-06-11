package script.block.player

import xlitekt.game.actor.render.Render.OverheadChat
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<OverheadChat>(13, 0x8) {
    buildFixedPacket(text.length + 1) {
        writeStringCp1252NullTerminated(text)
    }
}
