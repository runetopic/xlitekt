package xlitekt.cache.provider.vorbis

import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.instrument.InstrumentSample
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.__cd_ag
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.__cd_aj
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.__cd_aq
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.__cd_ar
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.__cd_av
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.__cd_p
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.__cd_r
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.__cd_v
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.iLog
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBit
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBits
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.setGlobalVorbis
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisBlockFlags
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisBlockMapping
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisBlockSize0
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisBlockSize1
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisFloors
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisMappings
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisResidues
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.window
import kotlin.math.sin

/**
 * @author Jordan Abraham
 */
data class VorbisEntryType(
    override val id: Int,
    var sampleRate: Int = 0,
    var sampleCount: Int = 0,
    var start: Int = 0,
    var end: Int = 0,
    var field368: Boolean = false,
    var packets: Array<ByteArray?>? = null,
    var samples: ByteArray? = null,
    var __y: Int = 0,
    var __t: FloatArray? = null,
    var sampleLength: Int = 0,
    var soundIndices: Int = 0,
    var hasReadFloor: Boolean = false,
    var field376: Int = 0
) : EntryType(id) {
    fun toInstrumentSample(var1: IntArray?): InstrumentSample? {
        if (var1 != null && var1[0] <= 0) return null
        if (this.samples == null) {
            __y = 0
            __t = FloatArray(vorbisBlockSize1)
            this.samples = ByteArray(sampleCount)
            sampleLength = 0
            soundIndices = 0
        }

        while (soundIndices < packets!!.size) {
            if (var1 != null && var1[0] <= 0) {
                return null
            }
            val data = getVorbisSampleData(soundIndices)
            if (data != null) {
                var var3 = sampleLength
                var var4 = data.size
                if (var4 > sampleCount - var3) {
                    var4 = sampleCount - var3
                }
                repeat(var4) {
                    var var6 = (128.0f + data[it] * 128.0f).toInt()
                    if (var6 and -256 != 0) {
                        var6 = var6.inv() shr 31
                    }
                    this.samples!![var3++] = (var6 - 128).toByte()
                }
                if (var1 != null) {
                    var1[0] -= var3 - sampleLength
                }
                sampleLength = var3
                ++soundIndices
            }
        }
        return InstrumentSample(sampleRate, samples!!, start, end, field368)
    }

    private fun getVorbisSampleData(var1: Int): FloatArray? {
        setGlobalVorbis(packets!![var1]!!, 0)
        readBit()
        val var2 = readBits(iLog(vorbisBlockMapping!!.size - 1))
        val blockFlag = vorbisBlockFlags!![var2]
        val n = if (blockFlag) vorbisBlockSize1 else vorbisBlockSize0
        val previousWindowFlag = if (blockFlag) readBit() != 0 else false
        val nextWindowFlag = if (blockFlag) readBit() != 0 else false

        val windowCenter = n shr 1
        val leftWindowStart = if (blockFlag && !previousWindowFlag) (n shr 2) - (vorbisBlockSize0 shr 2) else 0
        val leftWindowEnd = if (blockFlag && !previousWindowFlag) (vorbisBlockSize0 shr 2) + (n shr 2) else windowCenter
        val var10 = if (blockFlag && !previousWindowFlag) vorbisBlockSize0 shr 1 else n shr 1

        val rightWindowStart = if (blockFlag && !nextWindowFlag) n - (n shr 2) - (vorbisBlockSize0 shr 2) else windowCenter
        val rightWindowEnd = if (blockFlag && !nextWindowFlag) (vorbisBlockSize0 shr 2) + (n - (n shr 2)) else n
        val var13 = if (blockFlag && !nextWindowFlag) vorbisBlockSize0 shr 1 else n shr 1

        val vorbisMapping = vorbisMappings!![vorbisBlockMapping!![var2]]!!
        val hasReadFloor = !vorbisFloors!![vorbisMapping.submapFloor!![vorbisMapping.mappingMux]]!!.readSubmapFloor()
        repeat(vorbisMapping.submaps) {
            val pcm = window!!
            vorbisResidues!![vorbisMapping.submapResidue!![it]]!!.method834(pcm, n shr 1, hasReadFloor)
        }
        if (!hasReadFloor) {
            vorbisFloors!![vorbisMapping.submapFloor!![vorbisMapping.mappingMux]]!!.method728(window, n shr 1)
        }
        val var20: FloatArray
        if (hasReadFloor) {
            var index = n shr 1
            while (index < n) {
                window!![index] = 0.0f
                ++index
            }
        } else {
            val floorNumber = n shr 1
            val floorIndex = n shr 2
            val third = n shr 3
            var20 = window!!
            repeat(floorNumber) {
                var20[it] *= 0.5f
            }
            var var21 = floorNumber
            while (var21 < n) {
                var20[var21] = -var20[n - var21 - 1]
                ++var21
            }
            val var40 = if (blockFlag) __cd_ag!! else __cd_r!!
            val var22 = if (blockFlag) __cd_aq!! else __cd_p!!
            val var23 = if (blockFlag) __cd_aj!! else __cd_v!!
            val var24 = if (blockFlag) __cd_ar!! else __cd_av!!
            repeat(floorIndex) {
                val var26 = var20[it * 4] - var20[n - it * 4 - 1]
                val var27 = var20[it * 4 + 2] - var20[n - it * 4 - 3]
                val var28 = var40[it * 2]
                val var29 = var40[it * 2 + 1]
                var20[n - it * 4 - 1] = var26 * var28 - var27 * var29
                var20[n - it * 4 - 3] = var26 * var29 + var27 * var28
            }
            repeat(third) {
                val var26 = var20[floorNumber + it * 4 + 3]
                val var27 = var20[floorNumber + it * 4 + 1]
                val var28 = var20[it * 4 + 3]
                val var29 = var20[it * 4 + 1]
                var20[floorNumber + it * 4 + 3] = var26 + var28
                var20[floorNumber + it * 4 + 1] = var27 + var29
                val var30 = var40[floorNumber - 4 - it * 4]
                val var31 = var40[floorNumber - 3 - it * 4]
                var20[it * 4 + 3] = (var26 - var28) * var30 - (var27 - var29) * var31
                var20[it * 4 + 1] = (var27 - var29) * var30 + (var26 - var28) * var31
            }
            repeat(iLog(n - 1) - 3) {
                val var47 = n shr it + 2
                val var48 = 8 shl it
                repeat(2 shl it) { var49 ->
                    val var50 = n - var47 * var49 * 2
                    val var51 = n - var47 * (var49 * 2 + 1)
                    repeat(n shr it + 4) { var32 ->
                        val var33 = var32 * 4
                        val var34 = var20[var50 - 1 - var33]
                        val var35 = var20[var50 - 3 - var33]
                        val var36 = var20[var51 - 1 - var33]
                        val var37 = var20[var51 - 3 - var33]
                        var20[var50 - 1 - var33] = var34 + var36
                        var20[var50 - 3 - var33] = var35 + var37
                        val var38 = var40[var32 * var48]
                        val var39 = var40[var32 * var48 + 1]
                        var20[var51 - 1 - var33] = (var34 - var36) * var38 - (var35 - var37) * var39
                        var20[var51 - 3 - var33] = (var35 - var37) * var38 + (var34 - var36) * var39
                    }
                }
            }
            var size1 = 1
            while (size1 < third - 1) {
                val var47 = var24[size1]
                if (size1 < var47) {
                    val var48 = size1 * 8
                    val var49 = var47 * 8
                    var var30 = var20[var48 + 1]
                    var20[var48 + 1] = var20[var49 + 1]
                    var20[var49 + 1] = var30
                    var30 = var20[var48 + 3]
                    var20[var48 + 3] = var20[var49 + 3]
                    var20[var49 + 3] = var30
                    var30 = var20[var48 + 5]
                    var20[var48 + 5] = var20[var49 + 5]
                    var20[var49 + 5] = var30
                    var30 = var20[var48 + 7]
                    var20[var48 + 7] = var20[var49 + 7]
                    var20[var49 + 7] = var30
                }
                ++size1
            }
            repeat(floorNumber) {
                var20[it] = var20[it * 2 + 1]
            }
            repeat(third) {
                var20[n - 1 - it * 2] = var20[it * 4]
                var20[n - 2 - it * 2] = var20[it * 4 + 1]
                var20[n - floorIndex - 1 - it * 2] = var20[it * 4 + 2]
                var20[n - floorIndex - 2 - it * 2] = var20[it * 4 + 3]
            }
            repeat(third) {
                val var27 = var23[it * 2]
                val var28 = var23[it * 2 + 1]
                val var29 = var20[floorNumber + it * 2]
                val var30 = var20[floorNumber + it * 2 + 1]
                val var31 = var20[n - 2 - it * 2]
                val var52 = var20[n - 1 - it * 2]
                var var53 = var28 * (var29 - var31) + var27 * (var30 + var52)
                var20[floorNumber + it * 2] = (var29 + var31 + var53) * 0.5f
                var20[n - 2 - it * 2] = (var29 + var31 - var53) * 0.5f
                var53 = var28 * (var30 + var52) - var27 * (var29 - var31)
                var20[floorNumber + it * 2 + 1] = (var30 - var52 + var53) * 0.5f
                var20[n - 1 - it * 2] = (-var30 + var52 + var53) * 0.5f
            }
            repeat(floorIndex) {
                var20[it] = var20[floorNumber + it * 2] * var22[it * 2] + var20[floorNumber + it * 2 + 1] * var22[it * 2 + 1]
                var20[floorNumber - 1 - it] = var20[floorNumber + it * 2] * var22[it * 2 + 1] - var20[floorNumber + it * 2 + 1] * var22[it * 2]
            }
            repeat(floorIndex) {
                var20[it + (n - floorIndex)] = -var20[it]
            }
            repeat(floorIndex) {
                var20[it] = var20[floorIndex + it]
            }
            repeat(floorIndex) {
                var20[floorIndex + it] = -var20[floorIndex - it - 1]
            }
            repeat(floorIndex) {
                var20[floorNumber + it] = var20[n - it - 1]
            }
            var var46 = leftWindowStart
            while (var46 < leftWindowEnd) {
                val var27 = sin(((var46 - leftWindowStart).toDouble() + 0.5) / var10.toDouble() * 0.5 * 3.141592653589793).toFloat()
                window!![var46] *= sin(1.5707963267948966 * var27.toDouble() * var27.toDouble()).toFloat()
                ++var46
            }
            var46 = rightWindowStart
            while (var46 < rightWindowEnd) {
                val var27 = sin(((var46 - rightWindowStart).toDouble() + 0.5) / var13.toDouble() * 0.5 * 3.141592653589793 + 1.5707963267948966).toFloat()
                window!![var46] *= sin(1.5707963267948966 * var27.toDouble() * var27.toDouble()).toFloat()
                ++var46
            }
        }

        var var41: FloatArray? = null
        if (__y > 0) {
            val var18 = n + __y shr 2
            var41 = FloatArray(var18)
            if (!this.hasReadFloor) {
                repeat(field376) {
                    var41[it] += __t!![it + (__y shr 1)]
                }
            }
            if (!hasReadFloor) {
                var var19 = leftWindowStart
                while (var19 < n shr 1) {
                    var41[var41.size - (n shr 1) + var19] += window!![var19]
                    ++var19
                }
            }
        }

        val var54 = __t
        this.__t = window
        window = var54
        __y = n
        this.field376 = rightWindowEnd - (n shr 1)
        this.hasReadFloor = hasReadFloor
        return var41
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VorbisEntryType

        if (id != other.id) return false
        if (sampleRate != other.sampleRate) return false
        if (sampleCount != other.sampleCount) return false
        if (start != other.start) return false
        if (end != other.end) return false
        if (field368 != other.field368) return false
        if (packets != null) {
            if (other.packets == null) return false
            if (!packets.contentDeepEquals(other.packets)) return false
        } else if (other.packets != null) return false
        if (samples != null) {
            if (other.samples == null) return false
            if (!samples.contentEquals(other.samples)) return false
        } else if (other.samples != null) return false
        if (__y != other.__y) return false
        if (__t != null) {
            if (other.__t == null) return false
            if (!__t.contentEquals(other.__t)) return false
        } else if (other.__t != null) return false
        if (sampleLength != other.sampleLength) return false
        if (soundIndices != other.soundIndices) return false
        if (hasReadFloor != other.hasReadFloor) return false
        if (field376 != other.field376) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + sampleRate
        result = 31 * result + sampleCount
        result = 31 * result + start
        result = 31 * result + end
        result = 31 * result + field368.hashCode()
        result = 31 * result + (packets?.contentDeepHashCode() ?: 0)
        result = 31 * result + (samples?.contentHashCode() ?: 0)
        result = 31 * result + __y
        result = 31 * result + (__t?.contentHashCode() ?: 0)
        result = 31 * result + sampleLength
        result = 31 * result + soundIndices
        result = 31 * result + hasReadFloor.hashCode()
        result = 31 * result + field376
        return result
    }
}
