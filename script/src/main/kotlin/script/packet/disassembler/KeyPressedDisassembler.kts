package script.packet.disassembler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.packet.KeyPressedPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readByteSubtract
import xlitekt.shared.buffer.readUMedium

/**
 * @author Justin Kenney
 */
val logger = InlineLogger()
//        var c = -1 // -1 = logic does not support keycode
//        if (keycode in 1..12) c = Integer.parseInt("7${Integer.toHexString(keycode - 1)}", 16)
//        else if (keycode in 16..24) c = keycode + 33
//        else if (keycode == 25) c = 48
//
//        val text = if (c >= 0) KeyEvent.getKeyText(c) else ""

onPacketDisassembler(opcode = 97, size = -2) {
    var lastTime: Long = 0
    var keycodes = ArrayList<Int>()

    repeat(remaining.toInt() / 4) {
        readUMedium().let { if (it != 0) lastTime = it.toLong() }
        keycodes.add(readByteSubtract())
    }

    val packet = KeyPressedPacket(
        time = lastTime,
        keycodes = keycodes.toIntArray()
    )

//    logger.debug { packet }

    packet
}
