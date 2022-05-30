package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.item.Item
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
private val world by inject<World>()

onCommand("gp").use {
    inventory.add(Item(995, Int.MAX_VALUE)) {
        message { "Spawned max cash." }
    }
}

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
    zones().forEach {
        repeat(8) { x ->
            repeat(8) { z ->
                it.requestAddItem(FloorItem(4151, 1, Location(it.location.x + x, it.location.z + z, 0)))
            }
        }
    }
}

onCommand("deleteall").use {
    zones().forEach {
        it.items.forEach(it::requestRemoveItem)
    }
}

onCommand("test").use {
    zone()?.npcs?.forEach { println(it.entry?.name) }
}
