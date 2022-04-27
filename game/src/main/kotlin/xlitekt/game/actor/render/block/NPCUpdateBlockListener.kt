package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.ByteReadPacket
import kotlin.reflect.KClass
import xlitekt.game.actor.render.Render

/**
 * @author Jordan Abraham
 */
object NPCUpdateBlockListener {
    val listeners = mutableMapOf<KClass<*>, RenderingBlock>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : Render> onNPCUpdateBlock(index: Int, mask: Int, noinline packet: R.() -> ByteReadPacket) {
    NPCUpdateBlockListener.listeners[R::class] = RenderingBlock(index, mask, packet as Render.() -> ByteReadPacket)
}
