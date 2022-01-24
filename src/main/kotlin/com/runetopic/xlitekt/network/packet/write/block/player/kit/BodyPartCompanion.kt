package com.runetopic.xlitekt.network.packet.write.block.player.kit

import com.runetopic.xlitekt.game.actor.render.Render

data class BodyPartCompanion(
    val gender: Render.Appearance.Gender? = null,
    val id: Int
)
