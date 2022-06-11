package script.block.npc

import xlitekt.game.actor.render.Render.NPCCustomLevel
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeIntV1

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<NPCCustomLevel>(5, 0x200) {
    buildFixedPacket(4) {
        writeIntV1(level)
    }
}
