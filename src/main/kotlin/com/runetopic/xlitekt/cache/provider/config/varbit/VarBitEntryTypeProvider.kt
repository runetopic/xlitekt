package com.runetopic.xlitekt.cache.provider.config.varbit

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

class VarBitEntryTypeProvider : EntryTypeProvider<VarBitEntryType>() {
    private val logger = InlineLogger()

    override fun load() {
        js5Store
            .index(CONFIG_INDEX)
            .group(VARBIT_GROUP_ID)
            .files()
            .forEach { loadEntryType(ByteReadPacket(it.data), VarBitEntryType(it.id)) }
        logger.info { "Finished loading ${entries.size} VarBitEntry types." }
    }

    override fun loadEntryType(buffer: ByteReadPacket, type: VarBitEntryType) {
        while (true) {
            when (buffer.readUByte().toInt()) {
                0 -> break
                1 -> {
                    type.index = buffer.readUShort().toInt()
                    type.leastSignificantBit = buffer.readUByte().toInt()
                    type.mostSignificantBit = buffer.readUByte().toInt()
                }
            }
        }
    }
}
