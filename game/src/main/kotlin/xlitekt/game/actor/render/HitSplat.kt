package xlitekt.game.actor.render

import xlitekt.game.actor.Actor

/**
 * @author Jordan Abraham
 */
data class HitDamage(
    val source: Actor?,
    val type: HitType,
    val damage: Int,
    val delay: Int
) {
    fun isInteracting(actor: Actor, target: Actor?): Boolean = source == actor || actor == target
}
