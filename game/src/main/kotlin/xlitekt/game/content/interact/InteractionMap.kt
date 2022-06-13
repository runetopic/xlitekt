package xlitekt.game.content.interact

object InteractionMap {
    val handledObjectInteractions = mutableMapOf<Int, (GameObjectInteraction).() -> Unit>()
}

fun onGameObject(vararg ids: Int, block: (GameObjectInteraction).() -> Unit) {
    ids.forEach {
        InteractionMap.handledObjectInteractions[it] = block
    }
}
