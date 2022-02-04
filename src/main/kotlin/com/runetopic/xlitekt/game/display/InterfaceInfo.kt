package com.runetopic.xlitekt.game.display

/**
 * @author Tyler Telis
 */
enum class InterfaceInfo(
    val interfaceId: Int,
    private val fixedChildId: Int,
    private val resizableChildId: Int,
    private val resizableListChildId: Int,
    private val fullScreenChildId: Int = -1
) {
    MINI_MAP(InterfaceId.MINI_MAP, 24, 32, 32),
    CHAT_BOX(InterfaceId.CHAT_BOX, 10, 94, 91),
    UNKNOWN(InterfaceId.UNKNOWN, 31, 6, 6),
    CHAT_CHANNEL(InterfaceId.CHAT_CHANNEL, 86, 82, 79),
    COMBAT_OPTIONS(InterfaceId.COMBAT_OPTIONS, 79, 75, 72),
    SKILLS(InterfaceId.SKILLS, 80, 76, 73),
    CHARACTER_SUMMARY(InterfaceId.CHARACTER_SUMMARY, 81, 77, 74),
    INVENTORY(InterfaceId.INVENTORY, 82, 78, 75),
    WORN_EQUIPMENT(InterfaceId.WORN_EQUIPMENT, 83, 79, 76),
    PRAYER(InterfaceId.PRAYER, 84, 80, 77),
    MAGIC(InterfaceId.MAGIC, 85, 81, 78),
    FRIENDS(InterfaceId.FRIENDS, 88, 84, 81),
    ACCOUNT_MANAGEMENT(InterfaceId.ACCOUNT_MANAGEMENT, 87, 83, 80),
    LOGOUT(InterfaceId.LOGOUT, 89, 85, 82),
    SETTINGS(InterfaceId.SETTINGS, 90, 86, 83),
    EMOTES(InterfaceId.EMOTES, 91, 87, 84),
    MUSIC_PLAYER(InterfaceId.MUSIC_PLAYER, 92, 88, 85);

    fun childIdForLayout(mode: Layout) = when (mode) {
        Layout.FIXED -> fixedChildId
        Layout.RESIZABLE -> resizableChildId
        Layout.RESIZABLE_LIST -> resizableListChildId
        Layout.FULL_SCREEN -> fullScreenChildId
    }
}
