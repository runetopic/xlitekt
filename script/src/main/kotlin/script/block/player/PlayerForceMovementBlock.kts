package script.block.player

import xlitekt.game.actor.render.Render.ForceMovement
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<ForceMovement>(3, 0x4000) {
    allocate(10) {
        writeByteNegate { firstLocation.x - currentLocation.x }
        writeByteNegate { firstLocation.z - currentLocation.z }
        writeByteSubtract { secondLocation?.x?.minus(currentLocation.x) ?: 0 }
        writeByteSubtract { secondLocation?.z?.minus(currentLocation.z) ?: 0 }
        writeShortLittleEndianAdd { firstDelay * 30 }
        writeShortLittleEndianAdd { secondDelay * 30 }
        writeShortAdd { rotation }
    }
}
