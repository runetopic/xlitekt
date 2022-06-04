package xlitekt.game.content.item

import xlitekt.game.world.map.Location

/**
 * @author Jordan Abraham
 */
data class FloorItem(
    val id: Int,
    val amount: Int,
    val location: Location
)
