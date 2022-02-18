package com.runetopic.xlitekt.game.varp

import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.resource.VarBits
import com.runetopic.xlitekt.shared.resource.VarInfoResource
import com.runetopic.xlitekt.shared.resource.Varps

object VarMapping {
    private val varpInfo by inject<Varps>()
    private val varBitInfo by inject<VarBits>()

    fun varpInfo(name: String): VarInfoResource = varpInfo[name] ?: throw RuntimeException("VarPlayer $name is not currently registered in the system.")
    fun varBitInfo(name: String): VarInfoResource = varBitInfo[name] ?: throw RuntimeException("VarBit $name is not currently registered in the system.")
}
