package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.render.Render
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object NPCRenderingBlockListener {
    val listeners = mutableMapOf<KClass<*>, RenderingBlock>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : Render> onNPCUpdateBlock(index: Int, mask: Int, noinline packet: R.() -> ByteReadPacket) {
    NPCRenderingBlockListener.listeners[R::class] = RenderingBlock(index, mask, packet as Render.() -> ByteReadPacket)
}
