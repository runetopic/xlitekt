package com.runetopic.xlitekt.game.actor.render

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.tile.Tile

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

    fun faceTile(tile: Tile) {
        pendingUpdates += Render.FaceTile(tile)
    }

    fun hit(source: Actor?, type: HitType, damage: Int, delay: Int): Render.HitDamage {
        val hit = Render.HitDamage(source, type, damage, delay)
        pendingUpdates += hit
        return hit
    }

    fun spotAnimation(spotAnimation: Render.SpotAnimation) {
        pendingUpdates += spotAnimation
    }

    fun animate(
        id: Int,
        delay: Int = 0
    ) {
        pendingUpdates += Render.Animation(id, delay)
    }

    fun appearance(appearance: Render.Appearance): Render.Appearance {
        pendingUpdates += appearance
        return appearance
    }

    fun hasPendingUpdate(): Boolean = pendingUpdates.isNotEmpty()
    fun clearUpdates() = pendingUpdates.clear()
}
