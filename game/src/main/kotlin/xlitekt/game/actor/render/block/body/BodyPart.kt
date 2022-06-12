package xlitekt.game.actor.render.block.body

import xlitekt.game.actor.render.Render.Appearance.Gender

/**
 * @author Jordan Abraham
 */
@JvmInline
value class BodyPart(val id: Int) {
    fun packBodyPart(gender: Gender) = id + gender.id * 7

    companion object {
        val Head = BodyPart(0)
        val Jaw = BodyPart(1)
        val Torso = BodyPart(2)
        val Arms = BodyPart(3)
        val Hands = BodyPart(4)
        val Legs = BodyPart(5)
        val Feet = BodyPart(6)
    }
}
