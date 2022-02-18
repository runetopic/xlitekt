package com.runetopic.xlitekt.game.varp

import com.runetopic.xlitekt.game.varp.VarMapping.varpInfo
import com.runetopic.xlitekt.shared.resource.VarInfoResource

enum class VarType {
    VAR_PLAYER,
    VAR_BIT
}

abstract class Var(
    open val name: String,
    open val value: Int
) {
    abstract val info: VarInfoResource
    abstract val varType: VarType
}

sealed class VarPlayer(override val name: String, override val value: Int) : Var(name, value) {
    override val info by lazy { varpInfo(name) }
    override val varType: VarType get() = VarType.VAR_PLAYER

    class ScreenBrightness(override val value: Int) : VarPlayer("screen_brightness", value)
    class SpecialAttackEnergy(override val value: Int) : VarPlayer("special_attack_energy", value)
}

sealed class VarBit(override val name: String, override val value: Int) : Var(name, value) {
    override val info by lazy { VarMapping.varBitInfo(name) }

    override val varType: VarType get() = VarType.VAR_BIT

    class GoblinEmoteUnlock(override val value: Int) : VarBit("goblin_emote_unlock", value)
    class SideStonesArrangement(override val value: Int) : VarBit("side_stones_arrangement", value)
}
