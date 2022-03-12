package xlitekt.game.actor.movement

/**
 * @author Jordan Abraham
 */
enum class MovementSpeed(
    val id: Int
) {
    WALKING(1),
    RUNNING(2),
    TELEPORTING(Byte.MAX_VALUE.toInt());

    fun isWalking() = this == WALKING
    fun isRunning() = this == RUNNING
    fun isTeleporting() = this == TELEPORTING
}
