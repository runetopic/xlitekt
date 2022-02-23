package com.runetopic.xlitekt.cache.provider.config.param

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import com.runetopic.xlitekt.cache.provider.config.ScriptType
import com.runetopic.xlitekt.shared.buffer.readStringCp1252NullTerminated
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte

/**
 * @author Jordan Abraham
 */
class ParamEntryTypeProvider : EntryTypeProvider<ParamEntryType>() {
    override fun load(): Map<Int, ParamEntryType> = store
        .index(CONFIG_INDEX)
        .group(PARAM_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(ParamEntryType(it.id)) }
        .associateBy(ParamEntryType::id)

    override tailrec fun ByteReadPacket.loadEntryType(type: ParamEntryType): ParamEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> readUByte().toInt().toChar().apply {
                type.type = enumValues<ScriptType>().find { it.key == this }
            }
            2 -> type.defaultInt = readInt()
            4 -> type.autoDisable = false
            5 -> type.defaultString = readStringCp1252NullTerminated()
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
    }
}
