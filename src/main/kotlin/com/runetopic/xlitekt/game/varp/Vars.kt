package com.runetopic.xlitekt.game.varp

import com.runetopic.xlitekt.cache.Cache.entryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryType
import com.runetopic.xlitekt.cache.provider.config.varbit.VarBitEntryTypeProvider.Companion.mersennePrime
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket

class Vars(
    val player: Player,
    private val vars: MutableMap<Int, Var> = mutableMapOf()
) : MutableMap<Int, Var> by vars {

    fun login() {
        if (vars.isEmpty()) return

        vars.forEach { send(element = it.value) }
    }

    operator fun plusAssign(element: Var) {
        val id = element.info.id
        vars[id] = element
        send(element)
    }

    operator fun minusAssign(element: Var) {
        val id = element.info.id
        vars.remove(id)
    }

    private fun send(element: Var) {
        when (element.varType) {
            VarType.VAR_PLAYER -> sendVarp(element.info.id, element.value)
            VarType.VAR_BIT -> {
                val varBit = entryType<VarBitEntryType>(element.info.id) ?: return
                val varpValue = varpValue(varBit, element)
                sendVarp(varBit.index, varpValue)
            }
        }
    }

    private fun varpValue(
        entryType: VarBitEntryType,
        varp: Var
    ): Int {
        val mask = mersennePrime[entryType.mostSignificantBit - entryType.leastSignificantBit] shl entryType.leastSignificantBit
        val maskValue = if (varp.value < 0 || varp.value > mask) 0 else varp.value
        return (vars[varp.info.id]?.value ?: 0) and mask.inv() or maskValue shl entryType.leastSignificantBit and mask
    }

    private fun sendVarp(id: Int, value: Int) {
        if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
            player.write(VarpLargePacket(id, value))
        } else {
            player.write(VarpSmallPacket(id, value))
        }
    }
}

