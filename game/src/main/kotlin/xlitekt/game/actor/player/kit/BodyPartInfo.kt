package xlitekt.game.actor.player.kit

import io.ktor.utils.io.core.BytePacketBuilder
import xlitekt.game.actor.render.Render

abstract class BodyPartInfo(val index: Int) {
    abstract fun equipmentIndex(gender: Render.Appearance.Gender): Int
    abstract fun build(builder: BytePacketBuilder, gender: Render.Appearance.Gender, kit: Int)
}
