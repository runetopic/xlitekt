package xlitekt.game.world.map.zone

import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
data class ZoneUpdate(
    val bytes: ZoneUpdateType.() -> ByteReadPacket
)
