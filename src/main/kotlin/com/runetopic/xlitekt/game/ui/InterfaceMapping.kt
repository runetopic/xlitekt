package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.resource.InterfaceInfo
import com.runetopic.xlitekt.shared.resource.InterfaceInfoMap
import kotlin.reflect.KClass

/**
 * @author Tyler Telis
 */
object InterfaceMapping {
    private val interfaceInfoMap by inject<InterfaceInfoMap>()

    fun interfaceInfo(name: String): InterfaceInfo = interfaceInfoMap[name] ?: throw RuntimeException("Interface $name is not currently registered in the system.")

    val interfaceListeners = mutableMapOf<KClass<*>, UserInterfaceListener.() -> Unit>()

    inline fun <reified T : UserInterface> buildInterfaceListener(noinline listener: UserInterfaceListener.() -> Unit) {
        interfaceListeners[T::class] = listener
    }

    fun addInterfaceListener(userInterface: UserInterface, player: Player): UserInterfaceListener {
        val listener = UserInterfaceListener(player, userInterface)
        interfaceListeners[userInterface::class]?.invoke(listener)
        return listener
    }
}
