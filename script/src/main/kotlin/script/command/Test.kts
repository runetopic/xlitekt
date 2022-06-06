package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.actor.routeTeleport
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.item.Item
import xlitekt.game.content.projectile.Projectile
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.world.World
import xlitekt.game.world.map.CollisionMap
import xlitekt.game.world.map.GameObject
import xlitekt.game.world.map.Location
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
private val world by inject<World>()

onCommand("home").use {
    this.routeTeleport { Location(3221, 3219, 0) }
}

onCommand("moveup").use {
    this.routeTeleport { Location(location.x, location.z, location.level + 1) }
}

onCommand("movedown").use {
    this.routeTeleport { Location(location.x, location.z, location.level - 1) }
}

onCommand("climb").use {
    this.routeTeleport { Location(location.x, location.z+6400, location.level) }
}

onCommand("undoclimb").use {
    this.routeTeleport { Location(location.x, location.z-6400, location.level) }
}

onCommand("gp").use {
    inventory.addItem(Item(995, Int.MAX_VALUE)) {
        message { "Spawned max cash." }
    }
}

onCommand("add").use {
//    val item = FloorItem(4151, 1, location)
    val loc = GameObject(1124, location, 22, 0)
    if (zone().requestAddLoc(loc)) {
        CollisionMap.addObjectCollision(loc)
        message { "true" }
    }
}

onCommand("add2").use {
    val item = FloorItem(4151, 1, location)
//    val loc = GameObject(1124, location, 22, 0)
//    CollisionMap.addObjectCollision(loc)
    message { "${zone().requestAddObj(item)}" }
}

onCommand("delete").use {
    val loc = zone().locsSpawned.first()
    if (zone().requestRemoveLoc(loc)) {
        CollisionMap.removeObjectCollision(loc)
        message { "true" }
    }
}

onCommand("addall").use {
//    val zone = world.zone(Location(3222, 3222, 0))
//    zone.requestAddObj(FloorItem(4151, 1, Location(3222, 3222, 0)))
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
        zone.objsSpawned.forEach(zone::requestRemoveObj)
    }
}

onCommand("test").use {
    zone().npcs.forEach { println(it.entry?.name) }
}

onCommand("proj").use {
    val projectile = Projectile(1465, location, location.transform(6, 0, 0), 43, 31, 36, 16, 64)
    zone().requestAddMapProjAnim(projectile)
}

onCommand("design").use {
    interfaces += UserInterface.PlayerAppearanceDesigner
}
