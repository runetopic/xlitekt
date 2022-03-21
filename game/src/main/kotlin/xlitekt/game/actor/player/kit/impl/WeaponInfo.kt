package xlitekt.game.actor.player.kit.impl

import io.ktor.utils.io.core.BytePacketBuilder
import xlitekt.game.actor.player.kit.BodyPartInfo
import xlitekt.game.actor.render.Render
import xlitekt.game.content.container.equipment.Equipment

class WeaponInfo : BodyPartInfo(index = 3) {
    override fun equipmentIndex(gender: Render.Appearance.Gender): Int = Equipment.SLOT_WEAPON
    override fun build(builder: BytePacketBuilder, gender: Render.Appearance.Gender, kit: Int) = builder.writeByte(kit.toByte())
}
