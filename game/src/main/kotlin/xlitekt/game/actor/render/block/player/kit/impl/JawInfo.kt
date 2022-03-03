package xlitekt.game.actor.render.block.player.kit.impl

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeShort
import xlitekt.game.actor.player.Equipment
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.player.kit.BodyPartInfo

class JawInfo : BodyPartInfo(index = 11) {
    override fun equipmentIndex(gender: Render.Appearance.Gender): Int =
        if (gender === Render.Appearance.Gender.MALE) Equipment.SLOT_HEAD else Equipment.SLOT_CHEST

    override fun build(builder: BytePacketBuilder, gender: Render.Appearance.Gender, kit: Int) {
        if (gender === Render.Appearance.Gender.MALE) {
            builder.writeShort((0x100 + kit).toShort())
        } else {
            builder.writeByte(0)
        }
    }
}
