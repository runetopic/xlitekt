package com.runetopic.xlitekt.game.actor.render.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.actor.render.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeIntV1
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class NPCCustomLevelBlock : RenderingBlock<NPC, Render.NPCCustomLevel>(5, 0x200) {
    override fun build(actor: NPC, render: Render.NPCCustomLevel) = buildPacket {
        writeIntV1(render.level)
    }
}
