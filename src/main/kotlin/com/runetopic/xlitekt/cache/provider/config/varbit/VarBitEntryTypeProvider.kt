package com.runetopic.xlitekt.cache.provider.config.varbit

import com.runetopic.xlitekt.cache.provider.EntryTypeProvider
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import java.lang.IllegalArgumentException

class VarBitEntryTypeProvider : EntryTypeProvider<VarBitEntryType>() {

    init { generateMersennePrimeNumbers() }

    override fun load(): Set<VarBitEntryType> = store
        .index(CONFIG_INDEX)
        .group(VARBIT_CONFIG)
        .files()
        .map { ByteReadPacket(it.data).loadEntryType(VarBitEntryType(it.id)) }
        .toHashSet()

    override tailrec fun ByteReadPacket.loadEntryType(type: VarBitEntryType): VarBitEntryType {
        when (val opcode = readUByte().toInt()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> {
                type.index = readUShort().toInt()
                type.leastSignificantBit = readUByte().toInt()
                type.mostSignificantBit = readUByte().toInt()
            }
            else -> throw IllegalArgumentException("Missing opcode $opcode.")
        }
        return loadEntryType(type)
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
