package xlitekt.game.actor.render.block.body

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render

/**
 * @author Jordan Abraham
 */
object BodyPartListener {
    val listeners = mutableMapOf<Int, BodyPartInfo>()
}

fun onBodyPart(index: Int, bodyPart: BodyPart? = null, builder: BodyPartBuilder.() -> Unit) {
    BodyPartListener.listeners[index] = BodyPartInfo(bodyPart, builder)
}

data class BodyPartInfo(
    val bodyPart: BodyPart?,
    val builder: BodyPartBuilder.() -> Unit
)

class BodyPartBuilder(
    val kit: Int,
    val gender: Render.Appearance.Gender
) {
    var equipmentIndex: Int? = null
    var data: ByteReadPacket? = null

    inline fun equipmentSlot(equipmentIndex: () -> Int) {
        this.equipmentIndex = equipmentIndex.invoke()
    }

    inline fun bodyPart(builder: BytePacketBuilder.(Int) -> Unit) {
        this.data = buildPacket { builder.invoke(this, kit) }
    }
}
