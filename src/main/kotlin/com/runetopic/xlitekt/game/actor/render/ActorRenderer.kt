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

    fun hasPendingUpdate(): Boolean = pendingUpdates.isNotEmpty()
    fun clearUpdates() = pendingUpdates.clear()

    fun animate(animation: Render.Animation) {
        pendingUpdates += animation
    }

    fun customOptions(string1: String, string2: String, string3: String) {
        pendingUpdates += Render.UsernameOverride(string1, string2, string3)
    }

    fun appearance(appearance: Render.Appearance): Render.Appearance {
        pendingUpdates += appearance
        return appearance
    }

    fun faceActor(index: Int) {
        pendingUpdates += Render.FaceActor(index)
    }

    fun faceTile(tile: Tile) {
        pendingUpdates += Render.FaceTile(tile)
    }

    fun forceMove(forceMovement: Render.ForceMovement) {
        pendingUpdates += forceMovement
    }

    fun hit(source: Actor?, type: HitType, damage: Int, delay: Int): Render.HitDamage {
        val hit = Render.HitDamage(source, type, damage, delay)
        pendingUpdates += hit
        return hit
    }

    fun overheadChat(text: String) {
        pendingUpdates += Render.OverheadChat(text)
    }

    fun setCustomCombatLevel(level: Int) {
        pendingUpdates += Render.NPCCustomLevel(level)
    }

    fun recolor(recolor: Render.Recolor) {
        pendingUpdates += recolor
    }

    fun spotAnimation(spotAnimation: Render.SpotAnimation) {
        pendingUpdates += spotAnimation
    }

    fun transmog(id: Int) {
        pendingUpdates += Render.NPCTransmogrification(id)
    }

    fun setTemporaryMovementType(id: Int) {
        pendingUpdates += Render.TemporaryMovementType(id)
    }

    fun publicChat(message: String, packedEffects: Int) {
        pendingUpdates += Render.PublicChat(message, packedEffects)
    }
}
