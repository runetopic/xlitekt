package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.BytePacketBuilder
import xlitekt.game.actor.render.Render
import java.nio.ByteBuffer
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object PlayerRenderingBlockListener {
    val listeners = mutableMapOf<KClass<*>, RenderingBlock>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : Render> fixedPlayerUpdateBlock(index: Int, mask: Int, size: Int, noinline packet: R.(ByteBuffer) -> Unit) {
    PlayerRenderingBlockListener.listeners[R::class] = RenderingBlock(index, mask, size, fixed = packet as Render.(ByteBuffer) -> Unit)
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : Render> dynamicPlayerUpdateBlock(index: Int, mask: Int, size: Int, noinline packet: R.(BytePacketBuilder) -> Unit) {
    PlayerRenderingBlockListener.listeners[R::class] = RenderingBlock(index, mask, size, dynamic = packet as Render.(BytePacketBuilder) -> Unit)
}
