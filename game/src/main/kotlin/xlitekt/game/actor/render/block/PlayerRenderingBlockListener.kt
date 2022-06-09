package xlitekt.game.actor.render.block

import xlitekt.game.actor.render.Render
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object PlayerRenderingBlockListener {
    val listeners = mutableMapOf<KClass<*>, RenderingBlock>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : Render> onPlayerUpdateBlock(index: Int, mask: Int, noinline packet: R.() -> ByteArray) {
    PlayerRenderingBlockListener.listeners[R::class] = RenderingBlock(index, mask, packet as Render.() -> ByteArray)
}
