package xlitekt.game.content.interact

import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.content.item.FloorItem
import xlitekt.game.world.map.GameObject
import xlitekt.game.world.map.Location

abstract class InteractableTarget(
    open val location: Location,
) {
    fun width(): Int {
        return when (this) {
            is GameObject -> this.entry?.width ?: 1
            is FloorItem -> 1
            is NPC -> this.entry?.widthScale ?: 1
            is Player -> 1
            else -> -1
        }
    }

    fun height(): Int {
        return when (this) {
            is GameObject -> this.entry?.height ?: 1
            is FloorItem -> 1
            is NPC -> this.entry?.heightScale ?: 1
            is Player -> 1
            else -> -1
        }
    }

    fun size(): Int {
        return when (this) {
            is NPC -> this.entry?.size ?: 1
            is Player -> 1
            is GameObject -> {
                val width = this.entry?.width ?: 1
                val height = this.entry?.height ?: 1
                return width * height
            }
            else -> 1
        }
    }
}
