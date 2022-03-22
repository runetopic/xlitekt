package xlitekt.game.content.item

import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
data class GroundItem(
    override val id: Int,
    override val amount: Int,
    val location: Location
) : Item(id, amount)
