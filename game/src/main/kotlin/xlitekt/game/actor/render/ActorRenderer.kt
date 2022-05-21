package xlitekt.game.actor.render

import io.ktor.utils.io.core.ByteReadPacket
import kotlin.reflect.KClass

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 * @author Jordan Abraham
 */
class ActorRenderer {
    private val pendingUpdates = mutableMapOf<KClass<*>, Render>()
    private val cachedUpdates = mutableMapOf<Render, ByteReadPacket>()

    fun hasPendingUpdate(): Boolean = pendingUpdates.isNotEmpty()
    fun clearPendingUpdates() = pendingUpdates.clear()
    fun pendingUpdates() = pendingUpdates.values.toList()
    fun addPendingUpdate(render: Render) {
        pendingUpdates[render::class] = render
    }
    fun cachedUpdates() = cachedUpdates
    fun persistCachedUpdates() {
        cachedUpdates.entries.removeIf {
            it.key::class != Render.Appearance::class &&
                it.key::class != Render.FaceAngle::class &&
                it.key::class != Render.MovementType::class
        }
    }
}
