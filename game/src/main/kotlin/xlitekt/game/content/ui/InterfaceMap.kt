package xlitekt.game.content.ui

import kotlin.reflect.KClass
import xlitekt.game.actor.player.Player
import xlitekt.shared.inject
import xlitekt.shared.resource.InterfaceInfoMap
import xlitekt.shared.resource.InterfaceInfoResource

/**
 * @author Tyler Telis
 */
object InterfaceMap {
    private val interfaceInfoMap by inject<InterfaceInfoMap>()
    val map = mutableMapOf<KClass<*>, UserInterfaceListener.() -> Unit>()

    fun interfaceInfo(name: String): InterfaceInfoResource = interfaceInfoMap[name] ?: throw RuntimeException("Interface $name is not currently registered in the system.")

    fun addInterfaceListener(userInterface: UserInterface, player: Player): UserInterfaceListener {
        val listener = UserInterfaceListener(player, userInterface)
        map[userInterface::class]?.invoke(listener)
        return listener
    }
}

inline fun <reified T : UserInterface> onInterface(noinline listener: UserInterfaceListener.() -> Unit) {
    InterfaceMap.map[T::class] = listener
}
