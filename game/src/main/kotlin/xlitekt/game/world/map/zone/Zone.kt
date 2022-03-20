package xlitekt.game.world.map.zone

import xlitekt.game.actor.npc.NPC
import java.util.Collections

class Zone(
    val npcs: MutableList<NPC?> = Collections.synchronizedList(mutableListOf())
)
