package com.runetopic.xlitekt.game.actor.render

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.location.Location

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 * @author Jordan Abraham
 */
class ActorRenderer {

    val pendingUpdates = mutableListOf<Render>()

    fun hasPendingUpdate(): Boolean = pendingUpdates.isNotEmpty()
    fun clearUpdates() = pendingUpdates.clear()

    fun sequence(sequenceId: Int, delay: Int) {
        pendingUpdates += Render.Sequence(sequenceId, delay)
    }

    fun customOptions(prefix: String, infix: String, suffix: String) {
        pendingUpdates += Render.UsernameOverride(prefix, infix, suffix)
    }

    fun appearance(appearance: Render.Appearance): Render.Appearance {
        pendingUpdates += appearance
        return appearance
    }

    fun faceActor(index: Int) {
        pendingUpdates += Render.FaceActor(index)
    }

    fun faceTile(location: Location) {
        pendingUpdates += Render.FaceTile(location)
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

    fun spotAnimation(id: Int, speed: Int, height: Int, rotation: Int) {
        pendingUpdates += Render.SpotAnimation(id, speed, height, rotation)
    }

    fun transmog(id: Int) {
        pendingUpdates += Render.NPCTransmogrification(id)
    }

    fun movementType(running: Boolean) {
        pendingUpdates += Render.MovementType(running)
    }

    fun temporaryMovementType(id: Int) {
        pendingUpdates += Render.TemporaryMovementType(id)
    }

    fun publicChat(message: String, packedEffects: Int) {
        pendingUpdates += Render.PublicChat(message, packedEffects)
    }

    fun faceDirection(direction: Int) {
        pendingUpdates += Render.FaceDirection(direction)
    }
}
