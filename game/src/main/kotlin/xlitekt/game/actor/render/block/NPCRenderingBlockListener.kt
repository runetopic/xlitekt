package xlitekt.game.actor.render.block

import io.ktor.utils.io.core.BytePacketBuilder
import xlitekt.game.actor.render.Render
import java.nio.ByteBuffer
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
class NPCRenderingBlockListener : MutableMap<KClass<*>, RenderingBlock> by mutableMapOf() {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified R : Render> fixedNpcUpdateBlock(index: Int, mask: Int, size: Int, noinline packet: R.(ByteBuffer) -> Unit) {
        this[R::class] = RenderingBlock(index, mask, size, fixed = packet as Render.(ByteBuffer) -> Unit)
    }

    @Suppress("UNCHECKED_CAST")
    inline fun <reified R : Render> dynamicNpcUpdateBlock(index: Int, mask: Int, size: Int, noinline packet: R.(BytePacketBuilder) -> Unit) {
        this[R::class] = RenderingBlock(index, mask, size, dynamic = packet as Render.(BytePacketBuilder) -> Unit)
    }
}
