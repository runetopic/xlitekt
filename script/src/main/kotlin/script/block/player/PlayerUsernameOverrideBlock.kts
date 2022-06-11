package script.block.player

import xlitekt.game.actor.render.Render.UsernameOverride
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<UsernameOverride>(6, 0x100) {
    buildFixedPacket(prefix.length + infix.length + suffix.length + 3) {
        writeStringCp1252NullTerminated(prefix)
        writeStringCp1252NullTerminated(infix)
        writeStringCp1252NullTerminated(suffix)
    }
}
