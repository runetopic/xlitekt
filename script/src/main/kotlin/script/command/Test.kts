package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.item.Item
import xlitekt.game.content.projectile.Projectile
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
    val zone = world.zone(location)
//    val item = FloorItem(4151, 1, location)
    val loc = GameObject(1124, location, 22, 0)
    if (zone.requestAddLoc(loc)) {
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
    message { "${zone.requestAddObj(item)}" }
}

onCommand("delete").use {
    val zone = world.zone(Location(3222, 3222, 0))
    val loc = zone.locs.first()
    if (zone.requestRemoveLoc(loc)) {
        CollisionMap.removeObjectCollision(loc)
        message { "true" }
    }
}

onCommand("delete2").use {
    val zone = world.zone(Location(3220, 3220, 0))
    val item = zone.objs.first()
    message { "${zone.requestRemoveObj(item)}" }
}

onCommand("addall").use {
//    val zone = world.zone(Location(3222, 3222, 0))!!
//    zone?.requestAddItem(FloorItem(4151, 1, Location(3222, 3222, 0)))
    zones().forEach { zone ->
        repeat(8) { x ->
            repeat(8) { z ->
                zone.requestAddObj(FloorItem(4151, 1, Location(zone.location.x + x, zone.location.z + z, 0)))
            }
        }
    }
}

onCommand("deleteall").use {
    zones().forEach { zone ->
        zone.objs.forEach(zone::requestRemoveObj)
    }
}

onCommand("test").use {
    zone().npcs.forEach { println(it.entry?.name) }
}

onCommand("proj").use {
    val zone = world.zone(Location(3222, 3222, 0))
    val projectile = Projectile(1465, Location(3222, 3222, 0), Location(3216, 3216, 0), 43, 31, 36, 16, 64)
    zone.requestAddMapProjAnim(projectile)
}
