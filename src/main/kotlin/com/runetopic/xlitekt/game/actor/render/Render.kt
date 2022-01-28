package com.runetopic.xlitekt.game.actor.render

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPart
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPartColor
import java.util.EnumMap

/**
 * @author Jordan Abraham
 */
sealed class Render {
    data class Animation(
        val id: Int,
        val delay: Int
    ) : Render()

    data class OverheadChat(
        val text: String
    ) : Render()

    data class FaceTile(
        val tile: Tile
    ) : Render()

    data class HitDamage(
        val source: Actor?,
        val type: HitType,
        val damage: Int,
        val delay: Int
    ) : Render()

    data class Appearance(
        val gender: Gender = Gender.MALE,
        val headIcon: Int,
        val skullIcon: Int,
        val transform: Int,
        val hidden: Boolean
    ) : Render() {
        val bodyParts = EnumMap<BodyPart, Int>(BodyPart::class.java)
        val bodyPartColors = EnumMap<BodyPartColor, Int>(BodyPartColor::class.java)

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
