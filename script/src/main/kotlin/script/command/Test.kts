package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.content.item.FloorItem
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
private val world by inject<World>()

onCommand("add").use {
    val location = Location(3222, 3222, 0)
    val zone = world.zone(location)
    message { "${zone?.requestAddItem(FloorItem(4151, 1, location))}" }
}

onCommand("delete").use {
    val zone = world.zone(Location(3222, 3222, 0))
    val item = zone?.items?.first()
    if (item != null) {
        message { "${zone.requestRemoveItem(item)}" }
    }
}

onCommand("addall").use {
    val zone = world.zone(Location(3222, 3222, 0))!!
    val location = zone.location
    repeat(8) { x ->
        repeat(8) { z ->
            message { "${zone.requestAddItem(FloorItem(4151, 1, Location(location.x + x, location.z + z, 0)))}" }
        }
    }
}

onCommand("deleteall").use {
    val zone = world.zone(Location(3222, 3222, 0))!!
    zone.items.forEach(zone::requestRemoveItem)
}

onCommand("test").use {
    zone()?.npcs?.forEach { println(it.entry?.name) }
}
