package xlitekt.game.actor.render

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import kotlin.reflect.KClass

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 * @author Jordan Abraham
 */
class ActorRenderer {

    val pendingUpdates = mutableMapOf<KClass<*>, Render>()
    val cachedUpdates = mutableMapOf<Render, ByteReadPacket>()

    fun hasPendingUpdate(): Boolean = pendingUpdates.isNotEmpty()
    fun clearUpdates() = pendingUpdates.clear()

    fun sequence(sequenceId: Int, delay: Int) {
        pendingUpdates[Sequence::class] = Render.Sequence(sequenceId, delay)
    }

    fun customOptions(prefix: String, infix: String, suffix: String) {
        pendingUpdates[Render.UsernameOverride::class] = Render.UsernameOverride(prefix, infix, suffix)
    }

    fun appearance(appearance: Render.Appearance): Render.Appearance {
        pendingUpdates[Render.Appearance::class] = appearance
        return appearance
    }

    fun faceActor(index: Int) {
        pendingUpdates[Render.FaceActor::class] = Render.FaceActor(index)
    }

    fun faceTile(location: Location) {
        pendingUpdates[Render.FaceTile::class] = Render.FaceTile(location)
    }

    fun forceMove(forceMovement: Render.ForceMovement) {
        pendingUpdates[Render.ForceMovement::class] = forceMovement
    }

    fun hit(source: Actor?, type: HitType, damage: Int, delay: Int): Render.HitDamage {
        val hit = Render.HitDamage(source, type, damage, delay)
        pendingUpdates[Render.HitDamage::class] = hit
        return hit
    }

    fun overheadChat(text: String) {
        pendingUpdates[Render.OverheadChat::class] = Render.OverheadChat(text)
    }

    fun setCustomCombatLevel(level: Int) {
        pendingUpdates[Render.NPCCustomLevel::class] = Render.NPCCustomLevel(level)
    }

    fun recolor(recolor: Render.Recolor) {
        pendingUpdates[Render.Recolor::class] = recolor
    }

    fun spotAnimation(id: Int, speed: Int, height: Int, rotation: Int) {
        pendingUpdates[Render.SpotAnimation::class] = Render.SpotAnimation(id, speed, height, rotation)
    }

    fun transmog(id: Int) {
        pendingUpdates[Render.NPCTransmogrification::class] = Render.NPCTransmogrification(id)
    }

    fun movementType(running: Boolean) {
        pendingUpdates[Render.MovementType::class] = Render.MovementType(running)
    }

    fun temporaryMovementType(id: Int) {
        pendingUpdates[Render.TemporaryMovementType::class] = Render.TemporaryMovementType(id)
    }

    fun publicChat(message: String, packedEffects: Int) {
        pendingUpdates[Render.PublicChat::class] = Render.PublicChat(message, packedEffects)
    }

    fun faceDirection(direction: Int) {
        pendingUpdates[Render.FaceDirection::class] = Render.FaceDirection(direction)
    }
}
