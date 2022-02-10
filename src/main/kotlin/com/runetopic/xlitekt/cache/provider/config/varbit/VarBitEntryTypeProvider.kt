package com.runetopic.xlitekt.cache.provider.config.varbit

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.cache.hierarchy.index.group.file.File
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
            .map(::loadEntryType)
        logger.info { "Finished loading ${entries.size} VarBitEntry types." }
    }

    private fun loadEntryType(file: File) {
        val buffer = ByteReadPacket(file.data)

        while (true) {
            when (buffer.readByte().toInt() and 0xff) {
                0 -> break
                1 -> {
                    entries.add(
                        VarBitEntryType(
                            id = file.id,
                            index = buffer.readShort().toInt() and 0xffff,
                            leastSignificantBit = buffer.readByte().toInt() and 0xff,
                            mostSignificantBit = buffer.readByte().toInt() and 0xff
                        )
                    )
                }
            }
        }
    }
}
