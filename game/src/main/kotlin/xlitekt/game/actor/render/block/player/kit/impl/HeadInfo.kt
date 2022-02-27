package xlitekt.game.actor.render.block.player.kit.impl

import io.ktor.utils.io.core.BytePacketBuilder
import xlitekt.game.actor.player.Equipment
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.player.kit.BodyPartInfo

class HeadInfo : BodyPartInfo(index = 0) {
    override fun equipmentIndex(gender: Render.Appearance.Gender): Int = Equipment.SLOT_HEAD
    override fun build(builder: BytePacketBuilder, gender: Render.Appearance.Gender, kit: Int) = builder.writeByte(kit.toByte())
}
