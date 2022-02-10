package com.runetopic.xlitekt.game.actor.player.`var`

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.resource.VarBits
import com.runetopic.xlitekt.util.resource.Varps

class VarsManager(
    val player: Player
) {
    private val varps by inject<Varps>()
    private val varBits by inject<VarBits>()

    protected val vars = mutableMapOf<Int, Int>()

    fun setDefaults() {
    }
}
