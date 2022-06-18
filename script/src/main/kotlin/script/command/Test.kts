package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.actor.queueNormal
import xlitekt.game.content.command.CommandListener
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.item.Item
import xlitekt.game.content.projectile.Projectile
import xlitekt.game.content.skill.Skill
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.world.map.CollisionMap
import xlitekt.game.world.map.GameObject
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.transform
import xlitekt.game.world.map.withinDistance
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<CommandListener>().command("gp").use {
    inventory.addItem(Item(995, Int.MAX_VALUE)) {
        message { "Spawned max cash." }
    }
}

insert<CommandListener>().command("q").use {
    queueNormal {
        while (true) {
            val location = Location(3222, 3222)

            waitUntil {
                this@use.location.withinDistance(location, 2)
            }
            message { "Fuck you" }
        }
    }
}

insert<CommandListener>().command("xp").use {
    message { "${Skill.getLevelForXp(30_000.0)}" }
}

insert<CommandListener>().command("add").use {
//    val item = FloorItem(4151, 1, location)
    val loc = GameObject(1124, location, 22, 0)
    if (zone.requestAddLoc(loc)) {
        CollisionMap.addObjectCollision(loc)
        message { "true" }
    }
}

insert<CommandListener>().command("add2").use {
    val item = FloorItem(4151, 1, location)
//    val loc = GameObject(1124, location, 22, 0)
//    CollisionMap.addObjectCollision(loc)
    message { "${zone.requestAddObj(item)}" }
}

insert<CommandListener>().command("delete").use {
    val loc = zone.locsSpawned.first()
    if (zone.requestRemoveLoc(loc)) {
        CollisionMap.removeObjectCollision(loc)
        message { "true" }
    }
}

insert<CommandListener>().command("addall").use {
//    val zone = world.zone(Location(3222, 3222, 0))
//    zone.requestAddObj(FloorItem(4151, 1, Location(3222, 3222, 0)))
    zones.forEach { zone ->
        repeat(8) { x ->
            repeat(8) { z ->
                zone.requestAddObj(FloorItem(4151, 1, Location(zone.location.x + x, zone.location.z + z, 0)))
            }
        }
    }
}

insert<CommandListener>().command("deleteall").use {
    zones.forEach { zone ->
        zone.objsSpawned.forEach(zone::requestRemoveObj)
    }
}

insert<CommandListener>().command("test").use {
    zone.npcs.forEach { println(it.entry?.name) }
}

insert<CommandListener>().command("proj").use {
    val projectile = Projectile(1465, location, location.transform(6, 0, 0), 43, 31, 36, 16, 64)
    zone.requestAddMapProjAnim(projectile)
}

insert<CommandListener>().command("design").use {
    interfaces += UserInterface.PlayerAppearanceDesigner
}
