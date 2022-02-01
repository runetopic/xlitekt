package com.runetopic.xlitekt.game.ui

/**
 * @author Tyler Telis
 */
enum class InterfaceInfo(
    val interfaceId: Int,
    val fixedChildId: Int,
    val resizableChildId: Int,
    val resizableListChildId: Int
) {
    COMBAT_OPTIONS(InterfaceId.COMBAT_OPTIONS, -1, 75, -1),
    SKILLS(InterfaceId.SKILLS, -1, 76, -1),
    CHARACTER_SUMMARY(InterfaceId.CHARACTER_SUMMARY, -1, 77, -1),
    INVENTORY(InterfaceId.INVENTORY, -1, 78, -1),
    WORN_EQUIPMENT(InterfaceId.WORN_EQUIPMENT, -1, 79, -1),
    PRAYER(InterfaceId.PRAYER, -1, 80, -1),
    MAGIC(InterfaceId.MAGIC, -1, 81, -1),
    CHAT_CHANNEL(InterfaceId.CHAT_CHANNEL, -1, 82, -1),
    ACCOUNT_MANAGEMENT(InterfaceId.ACCOUNT_MANAGEMENT, -1, 83, -1),
    FRIENDS(InterfaceId.FRIENDS, -1, 84, -1),
    LOGOUT(InterfaceId.LOGOUT, -1, 85, -1),
    SETTINGS(InterfaceId.OPTIONS, -1, 86, -1),
    EMOTES(InterfaceId.EMOTES, -1, 87, -1),
    MUSIC_PLAYER(InterfaceId.MUSIC_PLAYER, -1, 88, -1);

    fun componentIdForDisplay(mode: DisplayMode) = when (mode) {
        DisplayMode.RESIZABLE -> resizableChildId
    }
}
