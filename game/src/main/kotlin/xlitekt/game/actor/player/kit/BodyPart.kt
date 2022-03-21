package xlitekt.game.actor.player.kit

import xlitekt.game.actor.render.Render

enum class BodyPart(private val id: Int) {
    HEAD(0),
    JAW(1),
    TORSO(2),
    ARMS(3),
    HANDS(4),
    LEGS(5),
    FEET(6);

    fun bodyPart(gender: Render.Appearance.Gender): Int = id + gender.mask * 7
}
