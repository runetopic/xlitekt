package script.skill.woodcutting

import xlitekt.game.actor.angleTo
import xlitekt.game.actor.animate
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.actor.queueWeak
import xlitekt.game.content.interact.InteractionMap.onGameObject
import xlitekt.game.content.item.Item
import xlitekt.game.world.map.GameObject
import java.util.Random

enumValues<TreeType>().forEach { tree ->
    onGameObject(*tree.locIds) {
        onInteraction("Chop down") {
            if (!inventory.hasSpace()) {
                message { "You don't have enough inventory space." }
                return@onInteraction
            }

            val items = inventory + equipment

            if (!items.any { it?.entry?.name?.contains("axe") == true }) {
                message { "You do not have an axe of which you have the woodcutting level to use." }
                return@onInteraction
            }

            chopTree(it, tree)
        }
    }
}

val random = Random()

fun Player.chopTree(gameObject: GameObject, treeType: TreeType) = queueWeak {
    message { "You swing you axe at the tree..." }

    while (inventory.hasSpace()) { // tree is dead or inventory full
        angleTo(gameObject)
        animate { 875 } // TODO this needs to go into the enum or something because the anim is what shows the axe.
        wait(4) // TODO random chance

        if (random.nextInt(1, 8) == 1) {
            return@queueWeak
        }

        if (random.nextInt(1, 2) == 1) {
            //  check tree reward amount if its 1 deplete the tree
            inventory.addItem(Item(1519, 1)) {
                message { "You get some willow logs." }
                spawnStump(gameObject, treeType)
            }
        }
    }
}

fun Player.spawnStump(gameObject: GameObject, treeType: TreeType) {
    zone().requestAddLoc(GameObject(treeType.stumpId, gameObject.location, 10, gameObject.rotation))
}
