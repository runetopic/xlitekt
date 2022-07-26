package xlitekt.game.content.ui

import xlitekt.game.actor.player.Player
import kotlin.reflect.KClass

/**
 * @author Tyler Telis
 */
class InterfaceListener : MutableMap<KClass<*>, (UserInterfaceListener).(Player) -> Unit> by mutableMapOf() {

    internal fun invokeUserInterface(userInterface: UserInterface, player: Player): UserInterfaceListener {
        val listener = UserInterfaceListener(player, userInterface)
        this[userInterface::class]?.invoke(listener, player)
        return listener
    }

    inline fun <reified T : UserInterface> userInterface(noinline listener: (UserInterfaceListener).(Player) -> Unit) {
        this[T::class] = listener
    }
}
