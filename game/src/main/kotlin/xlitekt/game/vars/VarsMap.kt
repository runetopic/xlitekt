package xlitekt.game.vars

import xlitekt.shared.inject
import xlitekt.shared.resource.VarBits
import xlitekt.shared.resource.VarInfoResource
import xlitekt.shared.resource.Varps

object VarsMap {
    private val varpInfo by inject<Varps>()
    private val varBitInfo by inject<VarBits>()

    fun varpInfo(name: String): VarInfoResource = varpInfo[name] ?: throw RuntimeException("VarPlayer $name is not currently registered in the system.")
    fun varBitInfo(name: String): VarInfoResource = varBitInfo[name] ?: throw RuntimeException("VarBit $name is not currently registered in the system.")
}
