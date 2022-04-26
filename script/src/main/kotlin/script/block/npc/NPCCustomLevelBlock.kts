package script.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.NPCCustomLevel
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeIntV1

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<NPCCustomLevel>(5, 0x200) {
    buildPacket {
        writeIntV1 { level }
    }
}
