package com.runetopic.xlitekt.game.varp

import com.runetopic.xlitekt.cache.Cache.entryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider.Companion.mersennePrime
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.resource.Varps

class VarsManager(
    val player: Player
) {
    private val injectedVarps by inject<Varps>()
    private val vars = mutableMapOf<Int, Int>()

    fun login() {
        setDefaults()
        if (vars.isEmpty()) return

        vars.forEach { sendVar(it.key, it.value) }
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

    private fun sendVar(id: Int, value: Int) {
        sendVar(VarType.VAR_PLAYER, id, value)
    }

    fun sendVarBit(id: Int, value: Int) {
        sendVar(VarType.VAR_BIT, id, value)
    }

    private fun sendVar(varType: VarType, id: Int, value: Int) {
        when (varType) {
            VarType.VAR_PLAYER -> sendVarp(id, value)
            VarType.VAR_BIT -> {
                val varBit = entryType<VarBitEntryType>(id) ?: return
                var mask = mersennePrime[varBit.mostSignificantBit - varBit.leastSignificantBit]
                var maskValue = value
                if (maskValue < 0 || maskValue > mask) maskValue = 0
                mask = mask shl varBit.leastSignificantBit
                val varpValue = vars.getOrDefault(varBit.index, 0) and mask.inv() or maskValue shl varBit.leastSignificantBit and mask
                vars[varBit.index] = varpValue
                sendVarp(varBit.index, varpValue)
            }
        }
    }

    private fun sendVarp(id: Int, value: Int) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
            player.write(VarpLargePacket(id, value))
        } else {
            player.write(VarpSmallPacket(id, value))
        }
    }
}
