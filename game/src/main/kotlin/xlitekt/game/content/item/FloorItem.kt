package xlitekt.game.content.item

import xlitekt.game.content.interact.InteractableTarget
import xlitekt.game.world.map.Location

/**
 * @author Jordan Abraham
 */
data class FloorItem(
    val id: Int,
    val amount: Int,
    override val location: Location
) : InteractableTarget(location)
