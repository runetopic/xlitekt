package script.zone

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.world.map.zone.ZoneUpdateType.ObjDeleteType
import xlitekt.game.world.map.zone.onZoneUpdate
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onZoneUpdate<ObjDeleteType> {
    buildPacket {
        writeByte { 7 }
        writeByteAdd { packedOffset }
        writeShortLittleEndian { itemId }
    }
}
