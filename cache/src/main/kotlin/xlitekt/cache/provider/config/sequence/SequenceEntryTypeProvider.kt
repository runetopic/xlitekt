package xlitekt.cache.provider.config.sequence

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class SequenceEntryTypeProvider : EntryTypeProvider<SequenceEntryType>() {

    override fun load(): Map<Int, SequenceEntryType> = store
        .index(CONFIG_INDEX)
        .group(SEQUENCE_CONFIG)
        .files()
        .filter { it.id != 8127 } // This sequence id is not proper or something?
        .map { ByteBuffer.wrap(it.data).loadEntryType(SequenceEntryType(it.id)) }
        .associateBy(SequenceEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: SequenceEntryType): SequenceEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> {
                val size = readUShort()
                type.frameLengths = buildList {
                    repeat(size) {
                        add(readUShort())
                    }
                }
                type.frameIds = buildList {
                    repeat(size) {
                        add(readUShort())
                    }
                    repeat(size) {
                        set(it, this[it] + (readUShort() shl 16))
                    }
                }
            }
            2 -> type.frameCount = readUShort()
            3 -> repeat(readUByte()) {
                discard(1) // Discard unknown.
            }
            4 -> type.field2091 = true
            5 -> type.field2092 = readUByte()
            6 -> type.shield = readUShort()
            7 -> type.weapon = readUShort()
            8 -> type.field2095 = readUByte()
            9 -> type.field2096 = readUByte()
            10 -> type.field2097 = readUByte()
            11 -> type.field2078 = readUByte()
            12 -> {
                val size = readUByte()
                type.chatFrameIds = buildList {
                    repeat(size) {
                        add(readUShort())
                    }
                    repeat(size) {
                        set(it, this[it] + (readUShort() shl 16))
                    }
                }
            }
            13 -> repeat(readUByte()) {
                discard(3) // Discard sound effects.
            }
            14 -> type.field2079 = readInt()
            15 -> repeat(readUShort()) {
                discard(5) // Discard unknown.
            }
            16 -> {
                type.field2082 = readUShort()
                type.field2083 = readUShort()
            }
            17 -> repeat(readUByte()) {
                discard(1) // Discard unknown.
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
