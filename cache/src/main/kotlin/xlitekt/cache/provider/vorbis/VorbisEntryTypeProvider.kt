package xlitekt.cache.provider.vorbis

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUByte
import xlitekt.cache.provider.EntryTypeProvider
import kotlin.math.cos
import kotlin.math.sin

/**
 * @author Jordan Abraham
 */
class VorbisEntryTypeProvider : EntryTypeProvider<VorbisEntryType>() {

    override fun load(): Map<Int, VorbisEntryType> {
        return store
            .index(VORBIS_INDEX)
            .groups()
            .map { ByteReadPacket(it.data).loadEntryType(VorbisEntryType(it.id)) }
            .associateBy(VorbisEntryType::id)
    }

    override fun ByteReadPacket.loadEntryType(type: VorbisEntryType): VorbisEntryType {
        if (type.id == 0) {
            setData(readBytes(), 0)
            vorbisBlockSize0 = 1 shl readBits(4)
            vorbisBlockSize1 = 1 shl readBits(4)
            field380 = FloatArray(vorbisBlockSize1)

            var var2: Int
            var var3: Int
            var var4: Int
            var var5: Int
            var var1 = 0
            while (var1 < 2) {
                var2 = if (var1 != 0) vorbisBlockSize1 else vorbisBlockSize0
                var3 = var2 shr 1
                var4 = var2 shr 2
                var5 = var2 shr 3
                val var12 = FloatArray(var3)
                for (var7 in 0 until var4) {
                    var12[var7 * 2] = cos((var7 * 4).toDouble() * 3.141592653589793 / var2.toDouble()).toFloat()
                    var12[var7 * 2 + 1] = -sin((var7 * 4).toDouble() * 3.141592653589793 / var2.toDouble()).toFloat()
                }
                val var13 = FloatArray(var3)
                for (var8 in 0 until var4) {
                    var13[var8 * 2] = cos((var8 * 2 + 1).toDouble() * 3.141592653589793 / (var2 * 2).toDouble()).toFloat()
                    var13[var8 * 2 + 1] = sin((var8 * 2 + 1).toDouble() * 3.141592653589793 / (var2 * 2).toDouble()).toFloat()
                }
                val var14 = FloatArray(var4)
                for (var9 in 0 until var5) {
                    var14[var9 * 2] = cos((var9 * 4 + 2).toDouble() * 3.141592653589793 / var2.toDouble()).toFloat()
                    var14[var9 * 2 + 1] = -sin((var9 * 4 + 2).toDouble() * 3.141592653589793 / var2.toDouble()).toFloat()
                }
                val var15 = IntArray(var5)
                val var10: Int = iLog(var5 - 1)
                for (var11 in 0 until var5) {
                    var15[var11] = method7074(var11, var10)
                }
                if (var1 != 0) {
                    field363 = var12
                    field378 = var13
                    field367 = var14
                    field384 = var15
                } else {
                    field381 = var12
                    field382 = var13
                    field383 = var14
                    field358 = var15
                }
                ++var1
            }

            var1 = readBits(8) + 1
            VorbisSample_codebooks = arrayOfNulls(var1)

            var2 = 0
            while (var2 < var1) {
                VorbisSample_codebooks!![var2] = VorbisCodebook()
                ++var2
            }

            var2 = readBits(6) + 1

            var3 = 0
            while (var3 < var2) {
                readBits(16)
                ++var3
            }

            var2 = readBits(6) + 1
            VorbisSample_floors = arrayOfNulls(var2)

            var3 = 0
            while (var3 < var2) {
                VorbisSample_floors!![var3] = VorbisFloor()
                ++var3
            }

            var3 = readBits(6) + 1
            VorbisSample_residues = arrayOfNulls(var3)

            var4 = 0
            while (var4 < var3) {
                VorbisSample_residues!![var4] = VorbisResidue()
                ++var4
            }

            var4 = readBits(6) + 1
            VorbisSample_mappings = arrayOfNulls(var4)

            var5 = 0
            while (var5 < var4) {
                VorbisSample_mappings!![var5] = VorbisMapping()
                ++var5
            }

            var5 = readBits(6) + 1
            vorbisBlockFlags = BooleanArray(var5)
            vorbisMapping = IntArray(var5)

            for (var6 in 0 until var5) {
                vorbisBlockFlags!![var6] = readBit() != 0
                readBits(16)
                readBits(16)
                vorbisMapping!![var6] = readBits(8)
            }
            return type
        }
        type.sampleRate = readInt()
        type.sampleCount = readInt()
        type.start = readInt()
        type.end = readInt()
        if (type.end < 0) {
            type.end = type.end.inv()
            type.field368 = true
        }
        val var3 = readInt()
        type.field375 = arrayOfNulls(var3)

        repeat(var3) {
            var size = 0
            var opcode: Int
            do {
                opcode = readUByte().toInt()
                size += opcode
            } while (opcode >= 255)

            val var7 = ByteArray(size)
            readBytes(size).copyInto(var7, 0)
            type.field375!![it] = var7
        }
        return type
    }

    internal companion object {
        var vorbisSample: ByteArray? = null
        var vorbisByteOffset: Int = 0
        var vorbisBitOffset: Int = 0
        var vorbisBlockSize0: Int = 0
        var vorbisBlockSize1: Int = 0
        var field380: FloatArray? = null
        var field363: FloatArray? = null
        var field378: FloatArray? = null
        var field367: FloatArray? = null
        var field381: FloatArray? = null
        var field382: FloatArray? = null
        var field383: FloatArray? = null
        var field384: IntArray? = null
        var field358: IntArray? = null
        var vorbisBlockFlags: BooleanArray? = null
        var vorbisMapping: IntArray? = null
        var VorbisSample_codebooks: Array<VorbisCodebook?>? = null
        var VorbisSample_floors: Array<VorbisFloor?>? = null
        var VorbisSample_residues: Array<VorbisResidue?>? = null
        var VorbisSample_mappings: Array<VorbisMapping?>? = null

        fun setData(bytes: ByteArray, offset: Int) {
            vorbisSample = bytes
            vorbisByteOffset = offset
            vorbisBitOffset = 0
        }

        fun readBit(): Int {
            val var0 = vorbisSample!![vorbisByteOffset].toInt() shr vorbisBitOffset and 1
            ++vorbisBitOffset
            vorbisByteOffset += vorbisBitOffset shr 3
            vorbisBitOffset = vorbisBitOffset and 7
            return var0
        }

        fun readBits(amount: Int): Int {
            var var1 = 0
            var var3: Int
            var var2 = 0
            var numBits = amount
            while (numBits >= 8 - vorbisBitOffset) {
                var3 = 8 - vorbisBitOffset
                val var4 = (1 shl var3) - 1
                var1 += vorbisSample!![vorbisByteOffset].toInt() shr vorbisBitOffset and var4 shl var2
                vorbisBitOffset = 0
                ++vorbisByteOffset
                var2 += var3
                numBits -= var3
            }

            if (numBits > 0) {
                var3 = (1 shl numBits) - 1
                var1 += vorbisSample!![vorbisByteOffset].toInt() shr vorbisBitOffset and var3 shl var2
                vorbisBitOffset += numBits
            }
            return var1
        }

        fun iLog(i: Int): Int {
            var var0 = i
            var var1 = 0
            if (var0 < 0 || var0 >= 65536) {
                var0 = var0 ushr 16
                var1 += 16
            }
            if (var0 >= 256) {
                var0 = var0 ushr 8
                var1 += 8
            }
            if (var0 >= 16) {
                var0 = var0 ushr 4
                var1 += 4
            }
            if (var0 >= 4) {
                var0 = var0 ushr 2
                var1 += 2
            }
            if (var0 >= 1) {
                var0 = var0 ushr 1
                ++var1
            }
            return var0 + var1
        }

        fun method7074(i: Int, i2: Int): Int {
            var var0 = i
            var var1 = i2
            var var2: Int
            var2 = 0
            while (var1 > 0) {
                var2 = var2 shl 1 or var0 and 1
                var0 = var0 ushr 1
                --var1
            }
            return var2
        }
    }
}
