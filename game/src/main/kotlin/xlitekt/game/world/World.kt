package xlitekt.game.world

import xlitekt.game.actor.NPCList
import xlitekt.game.actor.PlayerList
import xlitekt.game.actor.npc.NPC
import xlitekt.game.world.map.zone.Zones

class World(
    val players: PlayerList = PlayerList(MAX_PLAYERS),
    val npcs: NPCList = NPCList(MAX_NPCs)
) {
    fun addNPC(npc: NPC) {
        npcs.add(npc)
        Zones[npc.location]?.npcs?.add(npc)
    }

    companion object {
        const val MAX_PLAYERS = 2048
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}
