package com.runetopic.xlitekt.game.`var`

import com.runetopic.xlitekt.cache.Cache.entryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider.Companion.mersennePrime
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.resource.VarBits
import com.runetopic.xlitekt.util.resource.Varps

class VarsManager(
    val player: Player
) {
    private val injectedVarps by inject<Varps>()
    private val injectVarBits by inject<VarBits>()

    private val vars = mutableMapOf<Int, Int>()

    fun login() {
        setDefaults()
        if (vars.isEmpty()) return

        vars.forEach { sendVar(it.key, it.value, true, true) }
    }

    private fun setDefaults() {
        // TODO this should be set in the KTS files whenever we emit on onLogin event.
        // TODO i can probably store the actual string that we want to set and pull from during content.
        // So we can do like player.varManager.sendVarp("special_attack_energy", 100)
        val specialAttackEnergyVarp = injectedVarps["special_attack_energy"] ?: return
        vars[specialAttackEnergyVarp.id] = 100 * 10
        val screenBrightness = injectedVarps["screen_brightness"] ?: return
        vars[screenBrightness.id] = 0
    }

    private fun sendVar(id: Int, value: Int, saveVar: Boolean = false, onLogin: Boolean = false) {
        sendVar(VarType.VAR_PLAYER, id, value, saveVar, onLogin)
    }

    fun sendVarBit(id: Int, value: Int, save: Boolean = false, onLogin: Boolean = false) {
        sendVar(VarType.VAR_BIT, id, value, save, true)
    }

    private fun sendVar(varType: VarType, id: Int, value: Int, saveVar: Boolean, onLogin: Boolean) {
        when (varType) {
            VarType.VAR_PLAYER -> {
                if (onLogin || vars[id] != value) vars[id] = value
                sendVarp(id, value)
            }
            VarType.VAR_BIT -> {
                val varBit = entryType<VarBitEntryType>(id) ?: return
                var mask = mersennePrime[varBit.mostSignificantBit - varBit.leastSignificantBit]
                var maskValue = value
                if (maskValue < 0 || maskValue > mask) maskValue = 0
                mask = mask shl varBit.leastSignificantBit
                val varpValue = vars.getOrDefault(varBit.index, 0) and mask.inv() or maskValue shl varBit.leastSignificantBit and mask
                if (vars.getOrDefault(varBit.index, 0) == varpValue) return

                if (saveVar) vars[varBit.index] = varpValue
                sendVarp(varBit.index, varpValue)
            }
        }
    }

    private fun sendVarp(id: Int, value: Int) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
            player.client.writePacket(VarpLargePacket(id, value))
        } else {
            player.client.writePacket(VarpSmallPacket(id, value))
        }
    }

    fun varpValue(id: Int) = vars.getOrDefault(id, 0)

    fun varbitValue(id: Int): Int {
        val varBit = entryType<VarBitEntryType>(id) ?: return 0
        if (!vars.containsKey(varBit.index) || vars[varBit.index] == null) return 0
        return vars.getOrDefault(varBit.index, 0) shr varBit.leastSignificantBit and mersennePrime[varBit.mostSignificantBit - varBit.leastSignificantBit]
    }

    fun varBitSet(id: Int): Boolean {
        val varBit = entryType<VarBitEntryType>(id) ?: return false
        return vars.containsKey(varBit.index)
    }
}
