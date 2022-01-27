package com.runetopic.xlitekt.game.actor.render

import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 * @author Jordan Abraham
 */
class ActorRenderer {

    val pendingUpdates = CopyOnWriteArrayList<Render>()

    internal fun animate(
        animationIds: IntArray,
        speed: Int = 0
    ) {
        pendingUpdates += Render.Animation(animationIds.toSet(), speed)
    }

    internal fun appearance(
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
