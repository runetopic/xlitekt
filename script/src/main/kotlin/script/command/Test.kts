package script.command

import xlitekt.game.actor.animate
import xlitekt.game.actor.player.addExperience
import xlitekt.game.actor.player.message
import xlitekt.game.actor.queue
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.item.Item
import xlitekt.game.content.projectile.Projectile
import xlitekt.game.content.skill.Skill
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.world.map.CollisionMap
import xlitekt.game.world.map.GameObject
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.transform

/**
 * @author Jordan Abraham
 */
onCommand("gp").use {
    inventory.addItem(Item(995, Int.MAX_VALUE)) {
        message { "Spawned max cash." }
    }
}

onCommand("fletch").use {
    queue {
        val player = this@use
        val inventory = player.inventory

        if (!inventory.hasItem(946)) {
            message { "You need a knife to fletch fuck face." }
            return@queue
        }

        var logCount = inventory.count { it?.id == 1511 }

        if (logCount == 0) {
            message { "You need logs to fletch fuck face." }
            return@queue
        }

        while (inventory.hasItem(946) && inventory.hasItem(1511)) {
            val level = player.skills.level(Skill.FLETCHING)
            val experience = player.skills.xp(Skill.FLETCHING)

            message { "Level $level $experience" }

            logCount = inventory.count { it?.id == 1511 }

            message { "Logs $logCount" }
            animate { 1280 }
            val log = inventory.first { it?.id == 1511 } ?: return@queue

            inventory.removeItem(log) {
                inventory.addItem(Item(52, 3)) {
                    message { "You make some fucking arrow shafts because that's all you know how to make." }
                    addExperience(Skill.FLETCHING, 5_000.0)
                }
            }

            wait(5)
        }
    }
}

onCommand("xp").use {
    message { "${Skill.getLevelForXp(30_000.0)}" }
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
