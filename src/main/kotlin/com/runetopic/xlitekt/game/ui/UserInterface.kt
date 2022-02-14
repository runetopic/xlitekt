package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.ui.InterfaceMapping.interfaceInfo

sealed class UserInterface(val name: String) {
    val interfaceInfo by lazy { interfaceInfo(name) }

    object AccountManagement : UserInterface("account_management")
    object Settings : UserInterface("settings")
    object Inventory : UserInterface("inventory")
    object MiniMap : UserInterface("mini_map")
    object ChatBox : UserInterface("chat_box")
    object Logout : UserInterface("logout")
    object Emotes : UserInterface("emotes")
    object Magic : UserInterface("magic")
    object MusicPlayer : UserInterface("music_player")
    object Skills : UserInterface("skills")
    object WornEquipment : UserInterface("worn_equipment")
    object EquipmentBonuses : UserInterface("equipment_bonuses")
    object Friends : UserInterface("friends")
    object Prayer : UserInterface("prayer")
    object CombatOptions : UserInterface("combat_options")
    object CharacterSummary : UserInterface("character_summary")
    object UnknownOverlay : UserInterface("unknown_overlay")
    object ChatChannel : UserInterface("chat_channel")
}
