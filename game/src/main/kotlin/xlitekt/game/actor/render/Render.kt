package xlitekt.game.actor.render

import kotlinx.serialization.Serializable
import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.serializer.AppearanceSerializer
import xlitekt.game.actor.render.block.body.BodyPart
import xlitekt.game.actor.render.block.body.BodyPartColor
import xlitekt.game.content.container.equipment.Equipment
import xlitekt.game.world.map.Location
import java.util.Optional

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
sealed class Render {
    data class Sequence(
        val id: Int,
        val delay: Int
    ) : Render() {
        constructor(id: Int) : this(id, 0)
    }

    data class UsernameOverride(
        val prefix: String,
        val infix: String,
        val suffix: String
    ) : Render()

    data class SpotAnimation(
        val id: Int,
        val speed: Int,
        val height: Int,
        val rotation: Int
    ) : Render() {
        constructor(id: Int) : this(id, 0, 0, 0)
        fun packedMetaData(): Int = speed and 0xffff or (height shl 16) // TODO rotation is used?
    }

    data class PublicChat(
        val message: String,
        val packedEffects: Int,
        val rights: Int
    ) : Render()

    data class FaceActor(
        val index: Int
    ) : Render()

    data class FaceAngle(
        val angle: Int
    ) : Render()

    data class NPCTransmogrification(
        val id: Int
    ) : Render()

    data class Recolor(
        val hue: Int,
        val saturation: Int,
        val luminance: Int,
        val amount: Int,
        val startDelay: Int,
        val endDelay: Int
    ) : Render()

    data class ForceMovement(
        val currentLocation: Location,
        val firstLocation: Location,
        val secondLocation: Location?,
        val firstDelay: Int = 0,
        val secondDelay: Int = 0,
        val rotation: Int = 0
    ) : Render()

    data class OverheadChat(
        val text: String
    ) : Render()

    data class FaceLocation(
        val location: Location
    ) : Render()

    data class NPCCustomLevel(
        val level: Int
    ) : Render()

    data class Hit(
        val actor: Actor,
        val splats: List<HitSplat>,
        val bars: List<HitBar>,
        val tint: Boolean
    ) : Render() {
        constructor(actor: Actor, hits: List<HitSplat>, bars: List<HitBar>) : this(actor, hits, bars, false)
    }

    data class MovementType(
        val running: Boolean
    ) : Render()

    data class TemporaryMovementType(val id: Int) : Render()

    @Serializable(with = AppearanceSerializer::class)
    class Appearance : Render() {
        val bodyParts = HashMap<BodyPart, Int>(7)
        val bodyPartColors = HashMap<BodyPartColor, Int>(5)
        var gender = Gender.Male
        var displayName: String = ""

        var headIcon = Optional.empty<Int>()
        var skullIcon = Optional.empty<Int>()
        var transform = Optional.empty<Int>()
        var hidden = Optional.empty<Boolean>()
        lateinit var equipment: Equipment

        init {
            bodyParts[BodyPart.Head] = 0
            bodyParts[BodyPart.Jaw] = 10
            bodyParts[BodyPart.Torso] = 18
            bodyParts[BodyPart.Arms] = 26
            bodyParts[BodyPart.Hands] = 33
            bodyParts[BodyPart.Legs] = 36
            bodyParts[BodyPart.Feet] = 42
            bodyPartColors[BodyPartColor.Hair] = 0
            bodyPartColors[BodyPartColor.Torso] = 0
            bodyPartColors[BodyPartColor.Legs] = 0
            bodyPartColors[BodyPartColor.Feet] = 0
            bodyPartColors[BodyPartColor.Skin] = 0
        }

        fun isMale(): Boolean = gender == Gender.Male
        fun isFemale(): Boolean = gender == Gender.Female

        @JvmInline
        value class Gender(val id: Int) {
            companion object {
                val Male = Gender(0)
                val Female = Gender(1)
            }
        }
    }

    /**
     * Returns if a render has an alternative update.
     */
    fun hasAlternative() = this is Hit
}
