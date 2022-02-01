package com.runetopic.xlitekt.game.ui

/**
 * @author Tyler Telis
 */
enum class InterfaceInfo(
    val interfaceId: Int,
    private val fixedChildId: Int,
    private val resizableChildId: Int,
    private val resizableListChildId: Int
) {
    CHAT_BOX(InterfaceId.CHAT_BOX, 10, 94, -1),
    COMBAT_OPTIONS(InterfaceId.COMBAT_OPTIONS, 62, 75, -1),
    SKILLS(InterfaceId.SKILLS, 63, 76, -1),
    CHARACTER_SUMMARY(InterfaceId.CHARACTER_SUMMARY, 64, 77, -1),
    INVENTORY(InterfaceId.INVENTORY, 65, 78, 1),
    WORN_EQUIPMENT(InterfaceId.WORN_EQUIPMENT, 66, 79, 1),
    PRAYER(InterfaceId.PRAYER, 67, 80, 1),
    MAGIC(InterfaceId.MAGIC, 68, 81, 1),
    CHAT_CHANNEL(InterfaceId.CHAT_CHANNEL, 46, 82, 1),
    ACCOUNT_MANAGEMENT(InterfaceId.ACCOUNT_MANAGEMENT, 47, 83, -1),
    FRIENDS(InterfaceId.FRIENDS, 48, 84, 1),
    LOGOUT(InterfaceId.LOGOUT, 49, 85, -1),
    SETTINGS(InterfaceId.OPTIONS, 50, 86, -1),
    EMOTES(InterfaceId.EMOTES, 51, 87, -1),
    MUSIC_PLAYER(InterfaceId.MUSIC_PLAYER, 52, 88, -1);

    fun componentIdForDisplay(mode: DisplayMode) = when (mode) {
        DisplayMode.FIXED -> fixedChildId
        DisplayMode.RESIZABLE -> resizableChildId
        DisplayMode.RESIZABLE_LIST -> resizableListChildId
    }
}
