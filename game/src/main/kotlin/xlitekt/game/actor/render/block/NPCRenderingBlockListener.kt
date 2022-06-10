package xlitekt.game.actor.render.block

import xlitekt.game.actor.render.Render
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object NPCRenderingBlockListener {
    val listeners = mutableMapOf<KClass<*>, RenderingBlock>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : Render> onNPCUpdateBlock(index: Int, mask: Int, noinline packet: R.() -> ByteArray) {
    NPCRenderingBlockListener.listeners[R::class] = RenderingBlock(index, mask, packet as Render.() -> ByteArray)
}
