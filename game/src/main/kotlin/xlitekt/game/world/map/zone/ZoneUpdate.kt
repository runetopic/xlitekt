package xlitekt.game.world.map.zone

import xlitekt.game.packet.LocAddPacket
import xlitekt.game.packet.LocDelPacket
import xlitekt.game.packet.MapProjAnimPacket
import xlitekt.game.packet.ObjAddPacket
import xlitekt.game.packet.ObjDelPacket

/**
 * @author Jordan Abraham
 */
@JvmInline
internal value class ZoneUpdateIndex(
    val index: Int
)

internal object ZoneUpdate {
    val zoneUpdateMap = mapOf(
        ObjAddPacket::class to ZoneUpdateIndex(4),
        ObjDelPacket::class to ZoneUpdateIndex(7),
        LocAddPacket::class to ZoneUpdateIndex(6),
        LocDelPacket::class to ZoneUpdateIndex(0),
        MapProjAnimPacket::class to ZoneUpdateIndex(8)
    )
}
