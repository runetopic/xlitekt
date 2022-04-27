package xlitekt.game.actor.render.block.body

import xlitekt.game.actor.render.Render.Appearance.Gender

enum class BodyPart(private val id: Int) {
    HEAD(0),
    JAW(1),
    TORSO(2),
    ARMS(3),
    HANDS(4),
    LEGS(5),
    FEET(6);

    fun bodyPart(gender: Gender): Int = id + gender.mask * 7
}
