package com.runetopic.xlitekt.game.actor.render

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 * @author Jordan Abraham
 */
class ActorRenderer {

    val pendingUpdates = mutableListOf<Render>()

    fun overheadChat(text: String) {
        pendingUpdates += Render.OverheadChat(text)
    }

    fun animate(
        id: Int,
        delay: Int = 0
    ) {
        pendingUpdates += Render.Animation(id, delay)
    }

    fun appearance(
        gender: Render.Appearance.Gender,
        headIcon: Int,
        skullIcon: Int,
        hidden: Boolean
    ) {
        pendingUpdates += Render.Appearance(headIcon, gender, skullIcon, -1, hidden)
    }

    fun hasPendingUpdate(): Boolean = pendingUpdates.isNotEmpty()
    fun clearUpdates() = pendingUpdates.clear()
}
