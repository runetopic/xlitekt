package xlitekt.cache.provider.config.enum

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.cache.provider.config.ScriptType
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

class EnumEntryTypeProvider : EntryTypeProvider<EnumEntryType>() {
    override fun load(): Map<Int, EnumEntryType> = store
        .index(CONFIG_INDEX)
        .group(ENUM_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(EnumEntryType(it.id)) }
        .associateBy(EnumEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: EnumEntryType): EnumEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> readUByte().toChar().apply {
                type.keyType = enumValues<ScriptType>().find { it.key == this }
            }
            2 -> readUByte().toChar().apply {
                type.valType = enumValues<ScriptType>().find { it.key == this }
            }
            3 -> type.defaultString = readStringCp1252NullTerminated()
            4 -> type.defaultInt = readInt()
            5 -> {
                val size = readUShort()
                type.params = buildMap {
                    repeat(size) {
                        put(readInt(), readStringCp1252NullTerminated())
                    }
                }
                type.size = size
            }
            6 -> {
                val size = readUShort()
                type.params = buildMap {
                    repeat(size) {
                        put(readInt(), readInt())
                    }
                }
                type.size = size
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
