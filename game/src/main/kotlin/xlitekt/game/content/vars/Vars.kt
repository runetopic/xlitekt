package xlitekt.game.content.vars

import xlitekt.cache.provider.config.varbit.VarBitEntryType
import xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider
import xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider.Companion.mersennePrime
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.sendVarp
import xlitekt.shared.inject

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
class Vars(
    private val player: Player,
    private val vars: MutableMap<Int, Int> = mutableMapOf()
) : MutableMap<Int, Int> by vars {

    /**
     * Sends the vars saved to the player to the client.
     */
    fun login() {
        if (isEmpty()) return
        forEach { player.sendVarp(it.key, it.value) }
    }

    /**
     * Sets a value to a var.
     */
    operator fun set(key: Var, value: Int): Int = when (key.varType) {
        VarType.VAR_PLAYER -> {
            when (value) {
                0 -> vars.remove(key.info.id)
                else -> vars[key.info.id] = value
            }
            player.sendVarp(key.info.id, value)
            value
        }
        VarType.VAR_BIT -> {
            varbits.entryType(key.info.id)?.run {
                val parentValue = toVarpParent(this, value)
                when (parentValue) {
                    0 -> vars.remove(index)
                    else -> vars[index] = parentValue
                }
                player.sendVarp(index, parentValue)
                parentValue
            } ?: -1
        }
    }

    /**
     * Gets the value of a var. Defaults to 0 if var is not found.
     */
    operator fun get(key: Var): Int = when (key.varType) {
        VarType.VAR_PLAYER -> vars[key.info.id] ?: 0
        VarType.VAR_BIT -> varbits.entryType(key.info.id)?.let(::fromVarpParent) ?: 0
    }

    /**
     * Checks if this player has a var.
     */
    operator fun contains(key: Var): Boolean = when (key.varType) {
        VarType.VAR_PLAYER -> vars[key.info.id] != null
        VarType.VAR_BIT -> vars[varbits.entryType(key.info.id)?.index] != null
    }

    /**
     * Flips a var from 0 -> 1 or 1 -> 0 only.
     */
    fun flip(key: Var) = if (get(key) == 0) set(key, 1) else set(key, 0)

    /**
     * Gets the value of a varbit from the parent varp.
     */
    private fun fromVarpParent(entryType: VarBitEntryType): Int {
        if (!vars.containsKey(entryType.index)) return 0
        return vars[entryType.index]!! shr entryType.leastSignificantBit and mersennePrime[entryType.mostSignificantBit - entryType.leastSignificantBit]
    }

    /**
     * Finds the value to use for a varbit using the parent varp.
     */
    private fun toVarpParent(entryType: VarBitEntryType, value: Int): Int {
        val prime = mersennePrime[entryType.mostSignificantBit - entryType.leastSignificantBit] shl entryType.leastSignificantBit
        return (vars[entryType.index] ?: 0) and prime.inv() or (((if (value < 0 || value > prime) 0 else value) shl entryType.leastSignificantBit) and prime)
    }

    private companion object {
        /**
         * The cache varbits provider.
         */
        val varbits by inject<VarBitEntryTypeProvider>()
    }
}
