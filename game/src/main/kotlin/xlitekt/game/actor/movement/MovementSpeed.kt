package xlitekt.game.actor.movement

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MovementSpeed(val id: Int) {
    val walking get() = id == 1
    val running get() = id == 2
    val teleporting get() = id == 127

    companion object {
        val None = MovementSpeed(0)
        val Walking = MovementSpeed(1)
        val Running = MovementSpeed(2)
        val Teleporting = MovementSpeed(127)
    }
}
