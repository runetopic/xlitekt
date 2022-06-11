package xlitekt.game.content.interact

import xlitekt.game.actor.player.Player
import xlitekt.game.world.map.GameObject

class GameObjectInteraction(
    val gameObject: GameObject
) : InteractionScript() {
    var interactions = mutableMapOf<String, OnGameObject>()
    var examines = mutableMapOf<Int, OnGameObject>()

    fun interact(player: Player, option: String) {
        val actions = gameObject.entry?.actions ?: return

        if (interactions.isEmpty() || actions.isEmpty() || !actions.contains(option)) return

        val optionScript = interactions[option] ?: return

        optionScript.invoke(player, gameObject)
    }

    fun onInteraction(option: String, function: OnGameObject) {
        interactions[option] = function
    }

    fun examine(player: Player, gameObject: GameObject): Boolean {
        val examine = examines[gameObject.id] ?: return false
        examine.invoke(player, gameObject)
        return true
    }

    fun onExamine(function: OnGameObject) {
        examines[gameObject.id] = function
    }
}

typealias OnGameObject = (Player).(GameObject) -> Unit
