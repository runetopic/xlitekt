package com.runetopic.xlitekt.network.packet.write.block.player.kit

import com.runetopic.xlitekt.game.actor.render.Render
import io.ktor.utils.io.core.BytePacketBuilder

abstract class BodyPartInfo(val index: Int) {
    abstract fun equipmentIndex(gender: Render.Appearance.Gender): Int
    abstract fun build(builder: BytePacketBuilder, kit: BodyPartCompanion)
}
