package com.runetopic.xlitekt.game.actor.player.`var`

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.resource.VarBit
import com.runetopic.xlitekt.util.resource.VarPlayer
import org.koin.core.qualifier.named

class VarsManager(
    val player: Player
) {
    private val varps by inject<List<VarPlayer>>(named("varps"))
    private val varBits by inject<List<VarBit>>(named("varbits"))

    protected val vars = mutableMapOf<Int, Int>()

    fun setDefaults() {
    }
}
