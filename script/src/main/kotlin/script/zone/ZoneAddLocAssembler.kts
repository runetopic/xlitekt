package script.zone

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.world.map.zone.ZoneUpdateType.LocAddType
import xlitekt.game.world.map.zone.onZoneUpdate
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onZoneUpdate<LocAddType> {
    buildPacket {
        writeByte { 6 }
        writeByteSubtract { (shape shl 2) or (rotation and 0x3) }
        writeByteSubtract { packedOffset }
        writeShortLittleEndianAdd { locId }
    }
}
