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
    CHAT_CHANNEL(InterfaceId.CHAT_CHANNEL, 86, -1, -1),
    COMBAT_OPTIONS(InterfaceId.COMBAT_OPTIONS, 79, 75, -1),
    SKILLS(InterfaceId.SKILLS, 80, 80, -1),
    CHARACTER_SUMMARY(InterfaceId.CHARACTER_SUMMARY, 81, 77, -1),
    INVENTORY(InterfaceId.INVENTORY, 82, 82, 1),
    WORN_EQUIPMENT(InterfaceId.WORN_EQUIPMENT, 83, 79, 1),
    PRAYER(InterfaceId.PRAYER, 84, 84, 1),
    MAGIC(InterfaceId.MAGIC, 85, 85, 1),
//    CHAT_CHANNEL(InterfaceId.CHAT_CHANNEL, 31, 82, 1), this needs to be sent ontop of interface 707. This is the overlay for chat channel
    ACCOUNT_MANAGEMENT(InterfaceId.ACCOUNT_MANAGEMENT, 87, 83, -1),
    FRIENDS(InterfaceId.FRIENDS, 88, 84, 1),
    LOGOUT(InterfaceId.LOGOUT, 89, 85, -1),
    SETTINGS(InterfaceId.OPTIONS, 90, 86, -1),
    EMOTES(InterfaceId.EMOTES, 91, 87, -1),
    MUSIC_PLAYER(InterfaceId.MUSIC_PLAYER, 92, 88, -1);

    fun componentIdForDisplay(mode: DisplayMode) = when (mode) {
        DisplayMode.FIXED -> fixedChildId
        DisplayMode.RESIZABLE -> resizableChildId
        DisplayMode.RESIZABLE_LIST -> resizableListChildId
    }
}
