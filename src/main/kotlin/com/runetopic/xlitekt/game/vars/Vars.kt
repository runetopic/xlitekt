package com.runetopic.xlitekt.game.vars

import com.runetopic.xlitekt.cache.Cache.entryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider.Companion.mersennePrime
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.player.sendVarp

class Vars(
    val player: Player,
    private val vars: MutableMap<Var, Int> = mutableMapOf()
) : MutableMap<Var, Int> by vars {

    fun login() {
        if (isEmpty()) return
        forEach { set(it.key, it.value) }
    }

    fun set(element: Var, value: Int) {
        vars[element] = value
        when (element.varType) {
            VarType.VAR_PLAYER -> player.sendVarp(element.info.id, value)
            VarType.VAR_BIT -> {
                val varBitEntry = entryType<VarBitEntryType>(element.info.id) ?: return
                player.sendVarp(varBitEntry.index, value(varBitEntry, element, value))
            }
        }
    }

    fun value(element: Var): Int = vars[element] ?: 0

    private fun value(
        entryType: VarBitEntryType,
        element: Var,
        value: Int
    ): Int {
        val mask = mersennePrime[entryType.mostSignificantBit - entryType.leastSignificantBit] shl entryType.leastSignificantBit
        val maskValue = if (value < 0 || value > mask) 0 else value
        return (this[element] ?: 0) and mask.inv() or maskValue shl entryType.leastSignificantBit and mask
    }
}
