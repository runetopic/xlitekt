package com.runetopic.xlitekt.network.packet.write.block.player.kit.impl

import com.runetopic.xlitekt.game.actor.player.Equipment
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.write.block.player.kit.BodyPartCompanion
import com.runetopic.xlitekt.network.packet.write.block.player.kit.BodyPartInfo
import io.ktor.utils.io.core.BytePacketBuilder

class HeadInfo : BodyPartInfo(index = 0) {
    override fun equipmentIndex(gender: Render.Appearance.Gender): Int = Equipment.SLOT_HEAD
    override fun build(builder: BytePacketBuilder, kit: BodyPartCompanion) = builder.writeByte(kit.id.toByte())
}
