package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.item.Item
import xlitekt.game.world.World
import xlitekt.game.world.map.collision.CollisionMap
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.obj.GameObject
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
private val world by inject<World>()

onCommand("gp").use {
    inventory.addItem(Item(995, Int.MAX_VALUE)) {
        message { "Spawned max cash." }
    }
}

onCommand("add").use {
    val location = Location(3222, 3222, 0)
    val zone = world.zone(location)!!
//    val item = FloorItem(4151, 1, location)
    val loc = GameObject(1124, location, 22, 0)
    if (zone.requestAddObject(loc)) {
        CollisionMap.addObjectCollision(loc)
        message { "true" }
    }
}

onCommand("add2").use {
    val location = Location(3220, 3220, 0)
    val zone = world.zone(location)
    val item = FloorItem(4151, 1, location)
//    val loc = GameObject(1124, location, 22, 0)
//    CollisionMap.addObjectCollision(loc)
    message { "${zone?.requestAddItem(item)}" }
}

onCommand("delete").use {
    val zone = world.zone(Location(3222, 3222, 0))
    val loc = zone?.locs?.first()
    if (loc != null) {
        if (zone.requestDeleteObject(loc)) {
            CollisionMap.removeObjectCollision(loc)
            message { "true" }
        }
    }
}

onCommand("delete2").use {
    val zone = world.zone(Location(3220, 3220, 0))
    val item = zone?.objs?.first()
    if (item != null) {
        message { "${zone.requestRemoveItem(item)}" }
    }
}

onCommand("addall").use {
    val zone = world.zone(Location(3222, 3222, 0))!!
//    zone?.requestAddItem(FloorItem(4151, 1, Location(3222, 3222, 0)))
    repeat(8) { x ->
        repeat(8) { z ->
            zone.requestAddItem(FloorItem(4151, 1, Location(zone.location.x + x, zone.location.z + z, 0)))
        }
    }
}

onCommand("deleteall").use {
    zones().forEach { zone ->
        zone.objs.forEach(zone::requestRemoveItem)
    }
}

onCommand("test").use {
    zone()?.npcs?.forEach { println(it.entry?.name) }
}
