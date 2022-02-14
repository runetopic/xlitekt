package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.resource.InterfaceInfo
import com.runetopic.xlitekt.util.resource.InterfaceInfoMap
import kotlin.reflect.KClass

/**
 * @author Tyler Telis
 */
object InterfaceMapping {
    private val interfaceInfoMap by inject<InterfaceInfoMap>()

    val userInterfaces = listOf(
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
        UserInterface.Friends,
        UserInterface.Prayer,
        UserInterface.CombatOptions,
        UserInterface.CharacterSummary,
        UserInterface.UnknownOverlay,
        UserInterface.ChatChannel
    ).associateBy(UserInterface::id)

    fun interfaceInfo(name: String): InterfaceInfo = interfaceInfoMap[name] ?: throw RuntimeException("Interface $name is not currently registered in the system.")

    val interfaceListeners = mutableMapOf<KClass<*>, UserInterfaceListener.() -> Unit>()

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : UserInterface> buildInterfaceListener(noinline function: UserInterfaceListener.() -> Unit) {
        interfaceListeners[T::class] = function
    }
}
