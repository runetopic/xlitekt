package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.resource.InterfaceInfoMap
import com.runetopic.xlitekt.shared.resource.InterfaceInfoResource
import kotlin.reflect.KClass

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
