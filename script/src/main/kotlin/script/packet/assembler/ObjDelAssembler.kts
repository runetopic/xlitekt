package script.packet.assembler

// import xlitekt.shared.buffer.buildPacket
import xlitekt.game.packet.ObjDelPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketAssembler<ObjDelPacket>(opcode = 26, size = 3) {
    allocate(3) {
        writeByteAdd { packedOffset }
        writeShortLittleEndian { id }
    }
//    buildPacket {
//        writeBytes(createBuffer().drop(1)::toByteArray)
//    }
}

// onZoneUpdateBlock<ObjDelPacket> {
//    createBuffer()
// }
//
// fun ObjDelPacket.createBuffer() = ByteBuffer.allocate(4)
//    // byte
//    .put(7)
//    // byte add
//    .put((packedOffset + 128).toByte())
//    // short little endian
//    .put(id.toByte())
//    .put((id shr 8).toByte())
//    .array()
