package xlitekt.game.actor.player.kit.impl

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.player.kit.BodyPartInfo
import xlitekt.game.actor.render.Render
import xlitekt.game.content.container.equipment.Equipment

class ArmInfo : BodyPartInfo(index = 6) {
    override fun equipmentIndex(gender: Render.Appearance.Gender): Int = Equipment.SLOT_CHEST
    override fun build(builder: BytePacketBuilder, gender: Render.Appearance.Gender, kit: Int) = builder.writeShort((0x100 + kit).toShort())
}
