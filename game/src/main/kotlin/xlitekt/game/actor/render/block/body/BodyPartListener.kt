package xlitekt.game.actor.render.block.body

import io.ktor.util.moveToByteArray
import xlitekt.game.actor.render.Render
import xlitekt.game.content.container.equipment.Equipment
import java.nio.ByteBuffer

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

    inline fun bodyPart(builder: ByteBuffer.(Int) -> Unit) {
        val buffer = ByteBuffer.allocate(4)
        builder.invoke(buffer, kit)
        buffer.limit(buffer.position())
        buffer.rewind()
        this.data = buffer.moveToByteArray()
    }
}
