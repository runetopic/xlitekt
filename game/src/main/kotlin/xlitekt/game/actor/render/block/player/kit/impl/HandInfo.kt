package xlitekt.game.actor.render.block.player.kit.impl

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.player.Equipment
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.player.kit.BodyPartInfo

class HandInfo : BodyPartInfo(index = 9) {
    override fun equipmentIndex(gender: Render.Appearance.Gender): Int = Equipment.SLOT_HAND
    override fun build(builder: BytePacketBuilder, gender: Render.Appearance.Gender, kit: Int) = builder.writeShort((0x100 + kit).toShort())
}
