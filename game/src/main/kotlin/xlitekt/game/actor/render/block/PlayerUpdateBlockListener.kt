package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.ByteReadPacket
import kotlin.reflect.KClass
import xlitekt.game.actor.render.Render

/**
 * @author Jordan Abraham
 */
object PlayerUpdateBlockListener {
    val listeners = mutableMapOf<KClass<*>, RenderingBlock>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : Render> onPlayerUpdateBlock(index: Int, mask: Int, noinline packet: R.() -> ByteReadPacket) {
    PlayerUpdateBlockListener.listeners[R::class] = RenderingBlock(index, mask, packet as Render.() -> ByteReadPacket)
}
