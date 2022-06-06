package xlitekt.game.content.vars

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
    object HitSplatTinting : VarBit("hit_splat_tinting")
    object AppearanceDesignerGenderSelect : VarBit("appearance_designer_gender_select")
    object SkillGuide : VarBit("skill_guide")
}
