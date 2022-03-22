import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.Zones

/**
 * @author Jordan Abraham
 */
onCommand("test").use { arguments ->
    val zone = Zones[Location(3222, 3222, 0)]
    zone?.requestRemoveGroundItem(zone.groundItems.first()!!)
}
