package xlitekt.game.vars

sealed class VarPlayer(override val name: String) : Var(name) {
    override val info by lazy { VarsMap.varpInfo(name) }
    override val varType get() = VarType.VAR_PLAYER

    object ScreenBrightness : VarPlayer("screen_brightness")
    object SpecialAttackEnergy : VarPlayer("special_attack_energy")
    object ProfanityFilter : VarPlayer("profanity_filter")
    object ChatEffects : VarPlayer("chat_effects")
    object ToggleRun : VarPlayer("toggle_run")
    object SplitFriendsPrivateChat : VarPlayer("split_friends_private_chat")
    object SingleMouseButtonMode : VarPlayer("single_mouse_button_mode")
}
