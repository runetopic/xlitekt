package xlitekt.game.content.interact

import xlitekt.game.actor.player.Player
import xlitekt.game.world.map.GameObject

class GameObjectInteraction(
    val player: Player,
    val gameObject: GameObject,
    val option: String? = null
) : InteractionScript {
    var interactions = mutableMapOf<String, OnGameObject>()
    var examines = mutableMapOf<Int, OnGameObject>()

    fun onInteraction(option: String, function: OnGameObject) {
        interactions[option] = function
    }

    fun examine(gameObject: GameObject): Boolean {
        val examine = examines[gameObject.id] ?: return false
        examine.invoke(player, gameObject)
        return true
    }

    fun onExamine(function: OnGameObject) {
        examines[gameObject.id] = function
    }

    override fun execute() {
        val actions = gameObject.entry?.actions ?: return

        if (interactions.isEmpty() || actions.isEmpty() || !actions.contains(option)) return

        val optionScript = interactions[option] ?: return

        optionScript.invoke(player, gameObject)
    }
}

typealias OnGameObject = (Player).(GameObject) -> Unit
