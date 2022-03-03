package xlitekt.game.vars

import xlitekt.shared.resource.VarInfoResource

abstract class Var(
    open val name: String,
) {
    abstract val info: VarInfoResource
    abstract val varType: VarType
}
