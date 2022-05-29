package xlitekt.game.world.map.zone

import io.ktor.utils.io.core.ByteReadPacket
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object ZoneUpdateAssembler {
    val assemblers = mutableMapOf<KClass<*>, ZoneUpdate>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified R : ZoneUpdateType> onZoneUpdate(noinline packet: R.() -> ByteReadPacket) {
    ZoneUpdateAssembler.assemblers[R::class] = ZoneUpdate(packet as ZoneUpdateType.() -> ByteReadPacket)
}
