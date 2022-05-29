package script.zone

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.world.map.zone.ZoneUpdateType.ObjAddType
import xlitekt.game.world.map.zone.onZoneUpdate
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onZoneUpdate<ObjAddType> {
    buildPacket {
        writeByte { 4 }
        writeShort { itemId }
        writeByteSubtract { packedOffset }
        writeShortLittleEndian { amount }
    }
}
