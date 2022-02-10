package com.runetopic.xlitekt.cache.provider.config.varbit

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readShort

class VarBitEntryTypeProvider : EntryTypeProvider<VarBitEntryType>() {
    private val logger = InlineLogger()

    override fun load() {
        js5Store
            .index(CONFIG_INDEX)
            .group(VARBIT_GROUP_ID)
            .files()
            .forEach { loadEntryType(it.data, VarBitEntryType(it.id)) }
        logger.info { "Finished loading ${entries.size} VarBitEntry types." }
    }

    override fun loadEntryType(data: ByteArray, type: VarBitEntryType) {
        val buffer = ByteReadPacket(data)

        while (true) {
            when (buffer.readByte().toInt() and 0xff) {
                0 -> break
                1 -> {
                    type.index = buffer.readShort().toInt() and 0xffff
                    type.leastSignificantBit = buffer.readByte().toInt() and 0xff
                    type.mostSignificantBit = buffer.readByte().toInt() and 0xff
                }
            }
        }
    }
}
