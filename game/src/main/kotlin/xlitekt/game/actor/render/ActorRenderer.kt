package xlitekt.game.actor.render

import io.ktor.utils.io.core.ByteReadPacket
import kotlin.reflect.KClass

/**
 * @author Tyler Telis
 * @email <xlitersps@gmail.com>
 * @author Jordan Abraham
 */
class ActorRenderer {
    val pendingUpdates = mutableMapOf<KClass<*>, Render>()
    val cachedUpdates = mutableMapOf<Render, ByteReadPacket>()

    fun hasPendingUpdate(): Boolean = pendingUpdates.isNotEmpty()
    fun clearUpdates() = pendingUpdates.clear()
}
