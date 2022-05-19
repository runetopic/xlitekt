package xlitekt.game.actor.render

import kotlinx.serialization.Serializable
import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.serializer.AppearanceSerializer
import xlitekt.game.actor.render.block.body.BodyPart
import xlitekt.game.actor.render.block.body.BodyPartColor
import xlitekt.game.world.map.location.Location
import java.util.EnumMap

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
        val hits: List<HitDamage>,
        val bars: List<HitBarType>
    ) : Render()

    data class MovementType(
        val running: Boolean
    ) : Render()

    data class TemporaryMovementType(val id: Int) : Render()

    @Serializable(with = AppearanceSerializer::class)
    class Appearance : Render() {
        val bodyParts = EnumMap<BodyPart, Int>(BodyPart::class.java)
        val bodyPartColors = EnumMap<BodyPartColor, Int>(BodyPartColor::class.java)

        var gender: Gender = Gender.MALE
        var headIcon: Int = -1
        var skullIcon: Int = -1
        var transform: Int = -1
        var hidden: Boolean = false
        var displayName: String = ""

        init {
            bodyParts[BodyPart.HEAD] = 0
            bodyParts[BodyPart.JAW] = 10
            bodyParts[BodyPart.TORSO] = 18
            bodyParts[BodyPart.ARMS] = 26
            bodyParts[BodyPart.HANDS] = 33
            bodyParts[BodyPart.LEGS] = 36
            bodyParts[BodyPart.FEET] = 42
            bodyPartColors[BodyPartColor.HAIR] = 0
            bodyPartColors[BodyPartColor.TORSO] = 0
            bodyPartColors[BodyPartColor.LEGS] = 0
            bodyPartColors[BodyPartColor.FEET] = 0
            bodyPartColors[BodyPartColor.SKIN] = 0
        }

        fun isMale(): Boolean = gender == Gender.MALE

        fun isFemale(): Boolean = gender == Gender.FEMALE

        enum class Gender(val mask: Int) { MALE(0x0), FEMALE(0x1); }
    }
}
