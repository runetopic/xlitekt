package xlitekt.cache.provider.config.varbit

import xlitekt.cache.provider.EntryTypeProvider
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import java.nio.ByteBuffer

class VarBitEntryTypeProvider : EntryTypeProvider<VarBitEntryType>() {

    init { generateMersennePrimeNumbers() }

    override fun load(): Map<Int, VarBitEntryType> = store
        .index(CONFIG_INDEX)
        .group(VARBIT_CONFIG)
        .files()
        .map { ByteBuffer.wrap(it.data).loadEntryType(VarBitEntryType(it.id)) }
        .associateBy(VarBitEntryType::id)

    override tailrec fun ByteBuffer.loadEntryType(type: VarBitEntryType): VarBitEntryType {
        when (val opcode = readUByte()) {
            0 -> { assertEmptyAndRelease(); return type }
            1 -> {
                type.index = readUShort()
                type.leastSignificantBit = readUByte()
                type.mostSignificantBit = readUByte()
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
