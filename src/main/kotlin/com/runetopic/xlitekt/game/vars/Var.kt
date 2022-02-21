package com.runetopic.xlitekt.game.vars

import com.runetopic.xlitekt.game.vars.VarsMap.varpInfo
import com.runetopic.xlitekt.shared.resource.VarInfoResource

enum class VarType {
    VAR_PLAYER,
    VAR_BIT
}

abstract class Var(
    open val name: String,
) {
    abstract val info: VarInfoResource
    abstract val varType: VarType
}

sealed class VarPlayer(override val name: String) : Var(name) {
    override val info by lazy { varpInfo(name) }
    override val varType get() = VarType.VAR_PLAYER

    object ScreenBrightness : VarPlayer("screen_brightness")
    object SpecialAttackEnergy : VarPlayer("special_attack_energy")
    object ProfanityFilter : VarPlayer("profanity_filter")
    object ChatEffects : VarPlayer("chat_effects")
    object SplitFriendsPrivateChat : VarPlayer("split_friends_private_chat")
    object SingleMouseButtonMode : VarPlayer("single_mouse_button_mode")
}

sealed class VarBit(override val name: String) : Var(name) {
    override val info by lazy { VarsMap.varBitInfo(name) }

    override val varType get() = VarType.VAR_BIT

    object GoblinEmoteUnlock : VarBit("goblin_emote_unlock")
    object SideStonesArrangement : VarBit("side_stones_arrangement")
    object AdvancedSettingsCategory : VarBit("advanced_settings_category")
    object HidePrivateChatWhenChatboxHidden : VarBit("hide_private_chat_when_chatbox_hidden")
    object MiddleMouseButtonCameraControl : VarBit("middle_mouse_button_camera_control")
    object ShiftClickDropItems : VarBit("shift_click_drop_items")
    object MoveFollowerOptionsLowerDown : VarBit("move_follower_options_lower_down")
    object EscClosesCurrentModal : VarBit("esc_closes_current_modal")
    object ScrollWheelChangesZoomDistance : VarBit("scroll_wheel_changes_zoom_distance")
}
