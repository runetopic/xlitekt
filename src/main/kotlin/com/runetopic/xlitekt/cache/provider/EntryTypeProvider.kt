package com.runetopic.xlitekt.cache.provider

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.buffer.readStringCp1252NullTerminated
import com.runetopic.xlitekt.shared.buffer.readUMedium
import com.runetopic.xlitekt.shared.toBoolean
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte

abstract class EntryTypeProvider<R : EntryType> {
    internal val entries by lazy(::load)
    protected val store by inject<Js5Store>()

    abstract fun load(): Map<Int, R>
    abstract fun ByteReadPacket.loadEntryType(type: R): R
    open fun R.postLoadEntryType() {}

    fun size() = entries.size
    fun entryType(id: Int): R? = entries[id]
    fun entries(): Collection<R> = entries.values

    fun ByteReadPacket.readStringIntParameters(): Map<Int, Any> = buildMap {
        repeat(readUByte().toInt()) {
            val usingString = readUByte().toInt().toBoolean()
            put(readUMedium(), if (usingString) readStringCp1252NullTerminated() else readInt())
        }
    }

    fun ByteReadPacket.assertEmptyAndRelease() {
        if (remaining.toInt() != 0) throw IllegalStateException("The remaining buffer is not empty.")
        else release()
    }

    companion object {
        // Indexes.
        const val CONFIG_INDEX = 2
        const val INTERFACE_INDEX = 3
        const val MAP_INDEX = 5

        // Config groups.
        const val LOC_CONFIG = 6
        const val ENUM_CONFIG = 8
        const val NPC_CONFIG = 9
        const val OBJ_CONFIG = 10
        const val PARAM_CONFIG = 11
        const val SEQUENCE_CONFIG = 12
        const val VARBIT_CONFIG = 14
        const val HITSPLAT_CONFIG = 32
        const val HITBAR_CONFIG = 33
    }
}
