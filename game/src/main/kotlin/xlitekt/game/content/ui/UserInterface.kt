package xlitekt.game.content.ui

import xlitekt.shared.inject
import xlitekt.shared.resource.InterfaceInfoMap

sealed class UserInterface(val name: String) {
    val interfaceInfo by lazy { interfaceInfoMap[name] ?: throw RuntimeException("Interface $name is not currently registered in the system.") }

    object AccountManagement : UserInterface("account_management")
    object Settings : UserInterface("settings")
    object Inventory : UserInterface("inventory")
    object MiniMap : UserInterface("mini_map")
    object ChatBox : UserInterface("chat_box")
    object Logout : UserInterface("logout")
    object Emotes : UserInterface("emotes")
    object ItemsKeptOnDeath : UserInterface("items_kept_on_death")
    object Magic : UserInterface("magic")
    object MusicPlayer : UserInterface("music_player")
    object Skills : UserInterface("skills")
    object WornEquipment : UserInterface("worn_equipment")
    object EquipmentStats : UserInterface("equipment_stats")
    object EquipmentInventory : UserInterface("equipment_inventory")
    object Friends : UserInterface("friends")
    object Prayer : UserInterface("prayer")
    object GuidePrices : UserInterface("guide_prices")
    object CombatOptions : UserInterface("combat_options")
    object CharacterSummary : UserInterface("character_summary")
    object UnknownOverlay : UserInterface("unknown_overlay")
    object ChatChannel : UserInterface("chat_channel")
    object AdvancedSettings : UserInterface("advanced_settings")
    object PlayerAppearanceDesigner : UserInterface("player_appearance_designer")

    private companion object {
        val interfaceInfoMap by inject<InterfaceInfoMap>()
    }
}
