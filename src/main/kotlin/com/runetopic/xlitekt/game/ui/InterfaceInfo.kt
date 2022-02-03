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
    CHAT_BOX(InterfaceId.CHAT_BOX, 10, 94, 91),
    UNKNOWN(InterfaceId.UNKNOWN, 31, 6, 6),
    CHAT_CHANNEL(InterfaceId.CHAT_CHANNEL, 86, 82, 79),
    COMBAT_OPTIONS(InterfaceId.COMBAT_OPTIONS, 79, 75, 72),
    SKILLS(InterfaceId.SKILLS, 80, 76, 84),
    CHARACTER_SUMMARY(InterfaceId.CHARACTER_SUMMARY, 81, 77, 74),
    INVENTORY(InterfaceId.INVENTORY, 82, 78, 75),
    WORN_EQUIPMENT(InterfaceId.WORN_EQUIPMENT, 83, 79, 76),
    PRAYER(InterfaceId.PRAYER, 84, 80, 77),
    MAGIC(InterfaceId.MAGIC, 85, 81, 78),
    FRIENDS(InterfaceId.FRIENDS, 88, 84, 81),
    ACCOUNT_MANAGEMENT(InterfaceId.ACCOUNT_MANAGEMENT, 87, 83, 80),
    LOGOUT(InterfaceId.LOGOUT, 89, 85, 82),
    SETTINGS(InterfaceId.OPTIONS, 90, 86, 83),
    EMOTES(InterfaceId.EMOTES, 91, 87, 84),
    MUSIC_PLAYER(InterfaceId.MUSIC_PLAYER, 92, 88, 85);

    fun componentIdForDisplay(mode: DisplayMode) = when (mode) {
        DisplayMode.FIXED -> fixedChildId
        DisplayMode.RESIZABLE -> resizableChildId
        DisplayMode.RESIZABLE_LIST -> resizableListChildId
    }
}
