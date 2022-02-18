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
    override val varType: VarType get() = VarType.VAR_PLAYER

    object ScreenBrightness : VarPlayer("screen_brightness")
    object SpecialAttackEnergy : VarPlayer("special_attack_energy")
}

sealed class VarBit(override val name: String) : Var(name) {
    override val info by lazy { VarsMap.varBitInfo(name) }

    override val varType: VarType get() = VarType.VAR_BIT

    object GoblinEmoteUnlock : VarBit("goblin_emote_unlock")
    object SideStonesArrangement : VarBit("side_stones_arrangement")
}
