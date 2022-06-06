package xlitekt.game.actor.movement

/**
 * @author Jordan Abraham
 */
@JvmInline
value class MovementSpeed(val id: Int) {
    inline val walking get() = id == 1
    inline val running get() = id == 2
    inline val teleporting get() = id == 127

    companion object {
        val None = MovementSpeed(0)
        val Walking = MovementSpeed(1)
        val Running = MovementSpeed(2)
        val Teleporting = MovementSpeed(127)
    }
}
