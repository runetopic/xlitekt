package xlitekt.cache.provider.config.param

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.cache.provider.config.ScriptType
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readStringCp1252NullTerminated
import xlitekt.shared.buffer.readUByte
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
class ParamEntryTypeProvider : EntryTypeProvider<ParamEntryType>() {
    override fun load(): Map<Int, ParamEntryType> = store
        .index(CONFIG_INDEX)
        .group(PARAM_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(ParamEntryType(it.id)) }
        .associateBy(ParamEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: ParamEntryType): ParamEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> readUByte().toChar().apply {
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
