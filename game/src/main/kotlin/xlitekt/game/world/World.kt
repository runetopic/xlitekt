package xlitekt.game.world

import xlitekt.game.actor.NPCList
import xlitekt.game.actor.PlayerList
import xlitekt.game.actor.npc.NPC
import xlitekt.game.world.map.zone.Zones

class World {

    fun addNPC(npc: NPC) {
        npcs.add(npc)
        Zones[npc.location]?.npcs?.add(npc)
    }

    val players = PlayerList(MAX_PLAYERS)
    val npcs = NPCList(MAX_NPCs)

    fun npcIndex(npc: NPC) = npcs.indexOf(npc)

    companion object {
        const val MAX_PLAYERS = 2048
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}
