package script.skill.woodcutting

import xlitekt.game.actor.angleTo
import xlitekt.game.actor.animate
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.addExperience
import xlitekt.game.actor.player.message
import xlitekt.game.actor.queueWeak
import xlitekt.game.content.interact.onGameObject
import xlitekt.game.content.item.Item
import xlitekt.game.content.skill.Skill
import xlitekt.game.content.skill.Skill.Companion.interpolate
import xlitekt.game.world.map.GameObject
import java.security.SecureRandom
import java.util.Locale

enumValues<TreeType>().forEach { tree ->
    onGameObject(*tree.locIds) {
        onInteraction("Chop down") {
            if (!inventory.hasSpace()) {
                message { "You don't have enough inventory space to carry any more logs." }
                return@onInteraction
            }

            if (skills.level(Skill.WOODCUTTING) < tree.requiredLevel) {
                message { "You don't have a high enough woodcutting level to chop this." }
                return@onInteraction
            }

            val currentTool = currentTool() ?: return@onInteraction

            chopTree(it, currentTool, tree)
        }
    }
}

fun Player.chopTree(gameObject: GameObject, currentTool: AxeType, treeType: TreeType) = queueWeak {
    message { "You swing you axe at the tree..." }

    while (inventory.hasSpace()) { // tree is dead or inventory full
        angleTo(gameObject)
        animate { currentTool.sequenceId }
        wait(4)

        val (low, high) = currentTool.getLowAndHigh(treeType)
        val chance = interpolate(skills.level(Skill.WOODCUTTING), low, high)
        val random = SecureRandom().nextInt(256)

        if (chance < random) continue

        val added = inventory.addItem(Item(treeType.reward, 1)) {
            addExperience(Skill.WOODCUTTING, treeType.experience)
            message { "You get some ${this.entry?.name?.lowercase() ?: "logs"}." }
        }

        if (added && treeType.alwaysFelled()) {
            spawnStump(gameObject, treeType)
            return@queueWeak
        }

        val (depletionLow, depletionHigh) = 1 to 8 // TODO redwoods is 1 in 11

        if (added && SecureRandom().nextInt(depletionLow, depletionHigh) == 1) {
            spawnStump(gameObject, treeType)
            return@queueWeak
        }
    }
}

fun Player.currentTool(): AxeType? {
    val woodcuttingLevel = skills.level(Skill.WOODCUTTING)

    val currentAxeType = enumValues<AxeType>().reversed().filter { woodcuttingLevel >= it.requiredLevel }.firstOrNull { axeType ->
        (inventory + equipment).filterNotNull().any { axeType.id.contains(it.id) }
    }

    if (currentAxeType == null) {
        message { "You do not have an axe of which you have the woodcutting level to use." }
        return null
    }

    return currentAxeType
}

fun spawnStump(gameObject: GameObject, treeType: TreeType) {
    gameObject.zone.requestAddLoc(GameObject(treeType.stumpId, gameObject.location, 10, gameObject.rotation))
//    gameObject.zone.requestReplaceLoc(
//        existingGameObject = gameObject,
//        newGameObject = GameObject(treeType.stumpId, gameObject.location, 10, gameObject.rotation, true)
//    )
    // TODO Some timer to swap it back dunno where this would go.
}
