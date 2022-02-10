package com.runetopic.xlitekt.network.packet.assembler.block.player.kit.impl

import com.runetopic.xlitekt.game.actor.player.Equipment
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPartInfo
import io.ktor.utils.io.core.BytePacketBuilder

class HeadInfo : BodyPartInfo(index = 0) {
    override fun equipmentIndex(gender: Render.Appearance.Gender): Int = Equipment.SLOT_HEAD
    override fun build(builder: BytePacketBuilder, gender: Render.Appearance.Gender, kit: Int) = builder.writeByte(kit.toByte())
}
