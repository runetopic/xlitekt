package com.runetopic.xlitekt.network.packet.write.block.player.kit

import com.runetopic.xlitekt.game.actor.render.Render
import io.ktor.utils.io.core.BytePacketBuilder

interface BodyPartInfo {
    fun index(): Int
    fun equipmentIndex(gender: Render.Appearance.Gender): Int
    fun build(builder: BytePacketBuilder, kit: BodyPartCompanion)
}
