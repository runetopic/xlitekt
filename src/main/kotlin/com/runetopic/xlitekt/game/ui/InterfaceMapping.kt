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

    private val userInterfaces = listOf(
        UserInterface.AccountManagement,
        UserInterface.Settings,
        UserInterface.Inventory,
        UserInterface.MiniMap,
        UserInterface.ChatBox,
        UserInterface.Logout,
        UserInterface.Emotes,
        UserInterface.Magic,
        UserInterface.MusicPlayer,
        UserInterface.Skills,
        UserInterface.WornEquipment,
        UserInterface.EquipmentBonuses,
        UserInterface.Friends,
        UserInterface.Prayer,
        UserInterface.CombatOptions,
        UserInterface.CharacterSummary,
        UserInterface.UnknownOverlay,
        UserInterface.ChatChannel
    ).associateBy { it.interfaceInfo.id }

    fun interfaceInfo(name: String): InterfaceInfo = interfaceInfoMap[name] ?: throw RuntimeException("Interface $name is not currently registered in the system.")

    val interfaceListeners = mutableMapOf<KClass<*>, UserInterfaceListener.() -> Unit>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : UserInterface> buildInterfaceListener(noinline listener: UserInterfaceListener.() -> Unit) {
        interfaceListeners[T::class] = listener
    }

    fun userInterface(id: Int): UserInterface? = userInterfaces[id]

    fun addInterfaceListener(element: UserInterface, player: Player): UserInterfaceListener {
        val listener = UserInterfaceListener(player, element)
        interfaceListeners[element::class]?.invoke(listener)
        return listener
    }

    fun interfaceListener(element: UserInterface): (UserInterfaceListener.() -> Unit)? {
        return interfaceListeners[element::class]
    }
}
