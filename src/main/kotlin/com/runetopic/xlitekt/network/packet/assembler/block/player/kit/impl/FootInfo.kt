package com.runetopic.xlitekt.network.packet.assembler.block.player.kit.impl

import com.runetopic.xlitekt.game.actor.player.Equipment
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPartCompanion
import com.runetopic.xlitekt.network.packet.assembler.block.player.kit.BodyPartInfo
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeShort

class FootInfo : BodyPartInfo(index = 10) {
    override fun equipmentIndex(gender: Render.Appearance.Gender): Int = Equipment.SLOT_FOOT
    override fun build(builder: BytePacketBuilder, kit: BodyPartCompanion) = builder.writeShort((0x100 + kit.id).toShort())
}
