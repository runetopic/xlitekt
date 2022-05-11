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
            setGlobalVorbis(readBytes(), 0)
            vorbisBlockSize0 = 1 shl readBits(4)
            vorbisBlockSize1 = 1 shl readBits(4)
            window = FloatArray(vorbisBlockSize1)

            repeat(2) { i ->
                val floorCount = if (i != 0) vorbisBlockSize1 else vorbisBlockSize0
                val residueCount = floorCount shr 1
                val mappingCount = floorCount shr 2
                val timeCount = floorCount shr 3
                val trig1 = FloatArray(residueCount)
                repeat(mappingCount) {
                    trig1[it * 2] = cos((it * 4).toDouble() * 3.141592653589793 / floorCount.toDouble()).toFloat()
                    trig1[it * 2 + 1] = -sin((it * 4).toDouble() * 3.141592653589793 / floorCount.toDouble()).toFloat()
                }
                val trig2 = FloatArray(residueCount)
                repeat(mappingCount) {
                    trig2[it * 2] = cos((it * 2 + 1).toDouble() * 3.141592653589793 / (floorCount * 2).toDouble()).toFloat()
                    trig2[it * 2 + 1] = sin((it * 2 + 1).toDouble() * 3.141592653589793 / (floorCount * 2).toDouble()).toFloat()
                }
                val trig3 = FloatArray(mappingCount)
                repeat(timeCount) {
                    trig3[it * 2] = cos((it * 4 + 2).toDouble() * 3.141592653589793 / floorCount.toDouble()).toFloat()
                    trig3[it * 2 + 1] = -sin((it * 4 + 2).toDouble() * 3.141592653589793 / floorCount.toDouble()).toFloat()
                }
                val var15 = IntArray(timeCount)
                val var10 = iLog(timeCount - 1)
                repeat(timeCount) {
                    var15[it] = method7074(it, var10)
                }
                if (i != 0) {
                    __cd_ag = trig1
                    __cd_aq = trig2
                    __cd_aj = trig3
                    __cd_ar = var15
                } else {
                    __cd_r = trig1
                    __cd_p = trig2
                    __cd_v = trig3
                    __cd_av = var15
                }
            }

            vorbisCodebooks = arrayOfNulls(readBits(8) + 1)
            repeat(vorbisCodebooks!!.size) {
                vorbisCodebooks!![it] = VorbisCodebook()
            }

            repeat(readBits(6) + 1) {
                readBits(16)
            }

            vorbisFloors = arrayOfNulls(readBits(6) + 1)
            repeat(vorbisFloors!!.size) {
                vorbisFloors!![it] = VorbisFloor()
            }

            vorbisResidues = arrayOfNulls(readBits(6) + 1)
            repeat(vorbisResidues!!.size) {
                vorbisResidues!![it] = VorbisResidue()
            }

            vorbisMappings = arrayOfNulls(readBits(6) + 1)
            repeat(vorbisMappings!!.size) {
                vorbisMappings!![it] = VorbisMapping()
            }

            val blockSize = readBits(6) + 1
            vorbisBlockFlags = BooleanArray(blockSize)
            vorbisBlockMapping = IntArray(blockSize)
            repeat(blockSize) {
                vorbisBlockFlags!![it] = readBit() != 0
                readBits(16)
                readBits(16)
                vorbisBlockMapping!![it] = readBits(8)
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
        type.packets = arrayOfNulls(readInt())
        repeat(type.packets!!.size) {
            var size = 0
            var opcode: Int
            do {
                opcode = readUByte().toInt()
                size += opcode
            } while (opcode >= 255)

            val bytes = ByteArray(size)
            readBytes(size).copyInto(bytes, 0)
            type.packets!![it] = bytes
        }
        assertEmptyAndRelease()
        return type
    }

    internal companion object {
        private var vorbisSample: ByteArray? = null
        private var vorbisByteOffset: Int = 0
        private var vorbisBitOffset: Int = 0
        var vorbisBlockSize0: Int = 0
        var vorbisBlockSize1: Int = 0
        var window: FloatArray? = null
        var __cd_ag: FloatArray? = null
        var __cd_aq: FloatArray? = null
        var __cd_aj: FloatArray? = null
        var __cd_r: FloatArray? = null
        var __cd_p: FloatArray? = null
        var __cd_v: FloatArray? = null
        var __cd_ar: IntArray? = null
        var __cd_av: IntArray? = null
        var vorbisBlockFlags: BooleanArray? = null
        var vorbisBlockMapping: IntArray? = null
        var vorbisCodebooks: Array<VorbisCodebook?>? = null
        var vorbisFloors: Array<VorbisFloor?>? = null
        var vorbisResidues: Array<VorbisResidue?>? = null
        var vorbisMappings: Array<VorbisMapping?>? = null

        fun setGlobalVorbis(bytes: ByteArray, offset: Int) {
            vorbisSample = bytes
            vorbisByteOffset = offset
            vorbisBitOffset = 0
        }

        fun readBit(): Int {
            val value = vorbisSample!![vorbisByteOffset].toInt() shr vorbisBitOffset and 1
            ++vorbisBitOffset
            vorbisByteOffset += vorbisBitOffset shr 3
            vorbisBitOffset = vorbisBitOffset and 7
            return value
        }

        fun readBits(amount: Int): Int {
            var value = 0
            var offset: Int
            var index = 0
            var numBits = amount
            while (numBits >= 8 - vorbisBitOffset) {
                offset = 8 - vorbisBitOffset
                val var4 = (1 shl offset) - 1
                value += (vorbisSample!![vorbisByteOffset].toInt() shr vorbisBitOffset and var4) shl index
                vorbisBitOffset = 0
                ++vorbisByteOffset
                index += offset
                numBits -= offset
            }

            if (numBits > 0) {
                offset = (1 shl numBits) - 1
                value += (vorbisSample!![vorbisByteOffset].toInt() shr vorbisBitOffset and offset) shl index
                vorbisBitOffset += numBits
            }
            return value
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

        fun method7074(i: Int, var1: Int): Int {
            var var0 = i
            var value: Int
            value = 0
            for (index in var1 downTo 1) {
                value = (value shl 1) or (var0 and 1)
                var0 = var0 ushr 1
            }
            return value
        }
    }
}
