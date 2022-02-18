package com.runetopic.xlitekt.game.varp

import com.runetopic.xlitekt.game.varp.VarMapping.varpInfo
import com.runetopic.xlitekt.shared.resource.VarInfoResource

abstract class Var(
    open val name: String
) {
    abstract val value: Int
    abstract val info: VarInfoResource
    abstract val varType: VarType
}

sealed class VarPlayer(override val name: String) : Var(name) {
    override val info by lazy { varpInfo(name) }
    override val varType: VarType get() = VarType.VAR_PLAYER

    class ScreenBrightness(override val value: Int) : VarPlayer("screen_brightness")
    class SpecialAttackEnergy(override val value: Int) : VarPlayer("special_attack_energy")
}

sealed class VarBit(override val name: String) : Var(name) {
    override val info by lazy { VarMapping.varBitInfo(name) }

    override val varType: VarType get() = VarType.VAR_BIT

    class GoblinEmoteUnlock(override val value: Int) : VarBit("goblin_emote_unlock")
    class SideStonesArrangement(override val value: Int) : VarBit("side_stones_arrangement")
}
