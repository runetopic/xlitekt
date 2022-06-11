package xlitekt.game.content.interact

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.world.map.GameObject

object InteractionMap {
    val handledObjectInteractions = mutableMapOf<Int, (GameObjectInteraction).() -> Unit>()

    fun onGameObject(vararg ids: Int, block: (GameObjectInteraction).() -> Unit) = ids.forEach {
        handledObjectInteractions[it] = block
    }
}

fun Player.interact(clickedOption: String, gameObject: GameObject) {
    val gameScript = InteractionMap.handledObjectInteractions[gameObject.id] ?: return run {
        message { "Nothing interesting happens" }
    }

    val gameObjectInteraction = GameObjectInteraction(gameObject)
    gameScript.invoke(gameObjectInteraction) // TODO this needs to be extracted out. and handled like the docs Kris has mentioned.
    gameObjectInteraction.interact(this, clickedOption)
}

fun Player.examine(gameObject: GameObject): Boolean {
    val gameScript = InteractionMap.handledObjectInteractions[gameObject.id] ?: return false
    val gameObjectInteraction = GameObjectInteraction(gameObject)
    gameScript.invoke(gameObjectInteraction) // TODO this needs to be extracted out. and handled like the docs Kris has mentioned.
    gameObjectInteraction.examine(this, gameObject)
    return true
}


