package xlitekt.game.actor.render.block.body

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.render.Render
import xlitekt.game.content.container.equipment.Equipment

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
    val gender: Render.Appearance.Gender,
    val equipment: Equipment
) {
    var data: ByteArray? = null

    inline fun bodyPart(builder: BytePacketBuilder.(Int) -> Unit) {
        val bodyPart = buildPacket { builder.invoke(this, kit) }
        this.data = bodyPart.readBytes()
        bodyPart.release()
    }
}
