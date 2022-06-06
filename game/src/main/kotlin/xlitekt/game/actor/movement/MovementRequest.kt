package xlitekt.game.actor.movement

import it.unimi.dsi.fastutil.ints.IntArrayList

/**
 * @author Jordan Abraham
 */
data class MovementRequest(
    val reachAction: (() -> Unit)?,
    val waypoints: IntArrayList,
    val failed: Boolean,
    val alternative: Boolean,
)
