package xlitekt.cache.provider.config.sequence

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import xlitekt.cache.provider.EntryTypeProvider
import java.lang.IllegalArgumentException

/**
 * @author Jordan Abraham
 */
class SequenceEntryTypeProvider : EntryTypeProvider<SequenceEntryType>() {

    override fun load(): Map<Int, SequenceEntryType> = store
        .index(CONFIG_INDEX)
        .group(SEQUENCE_CONFIG)
        .files()
        .filter { it.id != 8127 } // This sequence id is not proper or something?
        .map { ByteReadPacket(it.data).loadEntryType(SequenceEntryType(it.id)) }
        .associateBy(SequenceEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: SequenceEntryType): SequenceEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> {
                val size = readUShort().toInt()
                type.frameLengths = buildList {
                    repeat(size) {
                        add(readUShort().toInt())
                    }
                }
                type.frameIds = buildList {
                    repeat(size) {
                        add(readUShort().toInt())
                    }
                    repeat(size) {
                        set(it, this[it] + readUShort().toInt() shl 16)
                    }
                }
            }
            2 -> type.frameCount = readUShort().toInt()
            3 -> repeat(readUByte().toInt()) {
                discard(1) // Discard unknown.
            }
            4 -> type.field2091 = true
            5 -> type.field2092 = readUByte().toInt()
            6 -> type.shield = readUShort().toInt()
            7 -> type.weapon = readUShort().toInt()
            8 -> type.field2095 = readUByte().toInt()
            9 -> type.field2096 = readUByte().toInt()
            10 -> type.field2097 = readUByte().toInt()
            11 -> type.field2078 = readUByte().toInt()
            12 -> {
                val size = readUByte().toInt()
                type.chatFrameIds = buildList {
                    repeat(size) {
                        add(readUShort().toInt())
                    }
                    repeat(size) {
                        set(it, this[it] + readUShort().toInt() shl 16)
                    }
                }
            }
            13 -> repeat(readUByte().toInt()) {
                discard(3) // Discard sound effects.
            }
            14 -> type.field2079 = readInt()
            15 -> repeat(readUShort().toInt()) {
                discard(5) // Discard unknown.
            }
            16 -> {
                type.field2082 = readUShort().toInt()
                type.field2083 = readUShort().toInt()
            }
            17 -> repeat(readUByte().toInt()) {
                discard(1) // Discard unknown.
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
