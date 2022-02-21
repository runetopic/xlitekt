package com.runetopic.xlitekt.game.vars

import com.runetopic.xlitekt.cache.entryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider.Companion.mersennePrime
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.player.sendVarp

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
class Vars(
    private val player: Player,
    private val vars: MutableMap<Var, Int> = mutableMapOf()
) : MutableMap<Var, Int> by vars {

    fun login() {
        if (isEmpty()) return
        forEach { set(it.key, it.value) }
    }

    override fun put(key: Var, value: Int): Int {
        vars[key] = value
        when (key.varType) {
            VarType.VAR_PLAYER -> {
                player.sendVarp(key.info.id, value)
                return value
            }
            VarType.VAR_BIT -> {
                entryType<VarBitEntryType>(key.info.id)?.run {
                    val varpValue = value(this, key, value)
                    player.sendVarp(index, varpValue)
                    return varpValue
                }
            }
        }
        return -1
    }

    fun flip(key: Var) = put(key, if (this[key] == 1) 0 else 1)

    private fun value(
        entryType: VarBitEntryType,
        key: Var,
        value: Int
    ): Int {
        val mask = mersennePrime[entryType.mostSignificantBit - entryType.leastSignificantBit] shl entryType.leastSignificantBit
        val maskValue = if (value < 0 || value > mask) 0 else value
        return (this[key] ?: 0) and mask.inv() or maskValue shl entryType.leastSignificantBit and mask
    }
}
