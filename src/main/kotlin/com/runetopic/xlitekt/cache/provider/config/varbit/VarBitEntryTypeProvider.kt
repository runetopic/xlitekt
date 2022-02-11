package com.runetopic.xlitekt.cache.provider.config.varbit

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort

class VarBitEntryTypeProvider : EntryTypeProvider<VarBitEntryType>() {

    override fun load(): List<VarBitEntryType> {
        generateMersennePrimeNumbers()
        return js5Store
            .index(CONFIG_INDEX)
            .group(VARBIT_GROUP_ID)
            .files()
            .map { loadEntryType(ByteReadPacket(it.data), VarBitEntryType(it.id)) }
    }

    override fun loadEntryType(buffer: ByteReadPacket, type: VarBitEntryType): VarBitEntryType {
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

        return type
    }

    companion object {
        val mersennePrime = IntArray(32)

        private fun generateMersennePrimeNumbers() {
            var i = 2
            (mersennePrime.indices).forEach {
                mersennePrime[it] = i - 1
                i += i
            }
        }
    }
}
