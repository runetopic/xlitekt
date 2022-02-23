package com.runetopic.xlitekt.cache.provider.config.enum

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.ScriptType
import com.runetopic.xlitekt.shared.buffer.readStringCp1252NullTerminated
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import java.lang.IllegalArgumentException

class EnumEntryTypeProvider : EntryTypeProvider<EnumEntryType>() {
    override fun load(): Map<Int, EnumEntryType> = store
        .index(CONFIG_INDEX)
        .group(ENUM_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(EnumEntryType(it.id)) }
        .associateBy(EnumEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: EnumEntryType): EnumEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> readUByte().toInt().toChar().apply {
                type.keyType = enumValues<ScriptType>().find { it.key == this }
            }
            2 -> readUByte().toInt().toChar().apply {
                type.valType = enumValues<ScriptType>().find { it.key == this }
            }
            3 -> type.defaultString = readStringCp1252NullTerminated()
            4 -> type.defaultInt = readInt()
            5 -> {
                val size = readUShort().toInt()
                type.params = buildMap {
                    repeat(size) {
                        put(readInt(), readStringCp1252NullTerminated())
                    }
                }
                type.size = size
            }
            6 -> {
                val size = readUShort().toInt()
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
