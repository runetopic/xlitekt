package xlitekt.cache.provider.vorbis

import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.instrument.InstrumentSample
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field358
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field363
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field367
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field378
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field380
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field381
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field382
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field383
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.field384
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
    var field375: Array<ByteArray?>? = null,
    var samples: ByteArray? = null,
    var field377: Int = 0,
    var field387: FloatArray? = null,
    var field371: Int = 0,
    var field391: Int = 0,
    var hasReadFloor: Boolean = false,
    var field376: Int = 0
) : EntryType(id) {
    fun toInstrumentSample(samples: IntArray?): InstrumentSample? {
        if (samples != null && samples[0] <= 0) return null
        if (this.samples == null) {
            field377 = 0
            field387 = FloatArray(vorbisBlockSize1)
            this.samples = ByteArray(sampleCount)
            field371 = 0
            field391 = 0
        }

        while (field391 < field375!!.size) {
            if (samples != null && samples[0] <= 0) {
                return null
            }
            val data = getVorbisSampleData(field391)
            if (data != null) {
                var var3 = field371
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
                if (samples != null) {
                    samples[0] -= var3 - field371
                }
                field371 = var3
                ++field391
            }
        }
        field387 = null
        val var7 = this.samples
        this.samples = null
        return InstrumentSample(sampleRate, var7!!, start, end, field368)
    }

    private fun getVorbisSampleData(var1: Int): FloatArray? {
        setGlobalVorbis(field375!![var1]!!, 0)
        readBit()
        val var2 = readBits(iLog(vorbisBlockMapping!!.size - 1))
        val var3 = vorbisBlockFlags!![var2]
        val var4 = if (var3) vorbisBlockSize1 else vorbisBlockSize0
        val var5 = if (var3) readBit() != 0 else false
        val var6 = if (var3) readBit() != 0 else false

        val var7 = var4 shr 1
        val var8 = if (var3 && !var5) (var4 shr 2) - (vorbisBlockSize0 shr 2) else 0
        val var9 = if (var3 && !var5) (vorbisBlockSize0 shr 2) + (var4 shr 2) else var7
        val var10 = if (var3 && !var5) vorbisBlockSize0 shr 1 else var4 shr 1

        val var11 = if (var3 && !var6) var4 - (var4 shr 2) - (vorbisBlockSize0 shr 2) else var7
        val var12 = if (var3 && !var6) (vorbisBlockSize0 shr 2) + (var4 - (var4 shr 2)) else var4
        val var13 = if (var3 && !var6) vorbisBlockSize0 shr 1 else var4 shr 1

        val vorbisMapping = vorbisMappings!![vorbisBlockMapping!![var2]]!!
        val hasReadFloor = !vorbisFloors!![vorbisMapping.submapFloor!![vorbisMapping.mappingMux]]!!.readSubmapFloor()
        repeat(vorbisMapping.submaps) {
            vorbisResidues!![vorbisMapping.submapResidue!![it]]!!.method834(field380!!, var4 shr 1, hasReadFloor)
        }
        if (!hasReadFloor) {
            vorbisFloors!![vorbisMapping.submapFloor!![vorbisMapping.mappingMux]]!!.method728(field380, var4 shr 1)
        }
        if (hasReadFloor) {
            var index = var4 shr 1
            while (index < var4) {
                field380!![index] = 0.0f
                ++index
            }
        } else {
            val first = var4 shr 1
            val second = var4 shr 2
            val third = var4 shr 3
            val var20 = field380!!
            repeat(first) {
                var20[it] *= 0.5f
            }
            var var21 = first
            while (var21 < var4) {
                var20[var21] = -var20[var4 - var21 - 1]
                ++var21
            }
            val var40 = if (var3) field363!! else field381!!
            val var22 = if (var3) field378!! else field382!!
            val var23 = if (var3) field367!! else field383!!
            val var24 = if (var3) field384!! else field358!!
            repeat(second) {
                val var26 = var20[it * 4] - var20[var4 - it * 4 - 1]
                val var27 = var20[it * 4 + 2] - var20[var4 - it * 4 - 3]
                val var28 = var40[it * 2]
                val var29 = var40[it * 2 + 1]
                var20[var4 - it * 4 - 1] = var26 * var28 - var27 * var29
                var20[var4 - it * 4 - 3] = var26 * var29 + var27 * var28
            }
            repeat(third) {
                val var26 = var20[first + it * 4 + 3]
                val var27 = var20[first + it * 4 + 1]
                val var28 = var20[it * 4 + 3]
                val var29 = var20[it * 4 + 1]
                var20[first + it * 4 + 3] = var26 + var28
                var20[first + it * 4 + 1] = var27 + var29
                val var30 = var40[first - 4 - it * 4]
                val var31 = var40[first - 3 - it * 4]
                var20[it * 4 + 3] = (var26 - var28) * var30 - (var27 - var29) * var31
                var20[it * 4 + 1] = (var27 - var29) * var30 + (var26 - var28) * var31
            }
            repeat(iLog(var4 - 1) - 3) {
                val var47 = var4 shr it + 2
                val var48 = 8 shl it
                repeat(2 shl it) { var49 ->
                    val var50 = var4 - var47 * var49 * 2
                    val var51 = var4 - var47 * (var49 * 2 + 1)
                    repeat(var4 shr it + 4) { var32 ->
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
            repeat(first) {
                var20[it] = var20[it * 2 + 1]
            }
            repeat(third) {
                var20[var4 - 1 - it * 2] = var20[it * 4]
                var20[var4 - 2 - it * 2] = var20[it * 4 + 1]
                var20[var4 - second - 1 - it * 2] = var20[it * 4 + 2]
                var20[var4 - second - 2 - it * 2] = var20[it * 4 + 3]
            }
            repeat(third) {
                val var27 = var23[it * 2]
                val var28 = var23[it * 2 + 1]
                val var29 = var20[first + it * 2]
                val var30 = var20[first + it * 2 + 1]
                val var31 = var20[var4 - 2 - it * 2]
                val var52 = var20[var4 - 1 - it * 2]
                var var53 = var28 * (var29 - var31) + var27 * (var30 + var52)
                var20[first + it * 2] = (var29 + var31 + var53) * 0.5f
                var20[var4 - 2 - it * 2] = (var29 + var31 - var53) * 0.5f
                var53 = var28 * (var30 + var52) - var27 * (var29 - var31)
                var20[first + it * 2 + 1] = (var30 - var52 + var53) * 0.5f
                var20[var4 - 1 - it * 2] = (-var30 + var52 + var53) * 0.5f
            }
            repeat(second) {
                var20[it] = var20[first + it * 2] * var22[it * 2] + var20[first + it * 2 + 1] * var22[it * 2 + 1]
                var20[first - 1 - it] = var20[first + it * 2] * var22[it * 2 + 1] - var20[first + it * 2 + 1] * var22[it * 2]
            }
            repeat(second) {
                var20[it + (var4 - second)] = -var20[it]
            }
            repeat(second) {
                var20[it] = var20[second + it]
            }
            repeat(second) {
                var20[second + it] = -var20[second - it - 1]
            }
            repeat(second) {
                var20[first + it] = var20[var4 - it - 1]
            }
            var var10000: FloatArray
            var var46 = var8
            while (var46 < var9) {
                val var27 = sin(((var46 - var8).toDouble() + 0.5) / var10.toDouble() * 0.5 * 3.141592653589793).toFloat()
                var10000 = field380!!
                var10000[var46] *= sin(1.5707963267948966 * var27.toDouble() * var27.toDouble()).toFloat()
                ++var46
            }
            var46 = var11
            while (var46 < var12) {
                val var27 = sin(((var46 - var11).toDouble() + 0.5) / var13.toDouble() * 0.5 * 3.141592653589793 + 1.5707963267948966).toFloat()
                var10000 = field380!!
                var10000[var46] *= sin(1.5707963267948966 * var27.toDouble() * var27.toDouble()).toFloat()
                ++var46
            }
        }

        var var41: FloatArray? = null
        if (field377 > 0) {
            val var18 = var4 + field377 shr 2
            var41 = FloatArray(var18)
            if (!this.hasReadFloor) {
                repeat(field376) {
                    var41[it] += field387!![it + (field377 shr 1)]
                }
            }
            if (!hasReadFloor) {
                var var19 = var8
                while (var19 < var4 shr 1) {
                    var41[var41.size - (var4 shr 1) + var19] += field380!![var19]
                    ++var19
                }
            }
        }

        val var54 = field387
        this.field387 = field380
        field380 = var54
        field377 = var4
        this.field376 = var12 - (var4 shr 1)
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
        if (field375 != null) {
            if (other.field375 == null) return false
            if (!field375.contentDeepEquals(other.field375)) return false
        } else if (other.field375 != null) return false
        if (samples != null) {
            if (other.samples == null) return false
            if (!samples.contentEquals(other.samples)) return false
        } else if (other.samples != null) return false
        if (field377 != other.field377) return false
        if (field387 != null) {
            if (other.field387 == null) return false
            if (!field387.contentEquals(other.field387)) return false
        } else if (other.field387 != null) return false
        if (field371 != other.field371) return false
        if (field391 != other.field391) return false
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
        result = 31 * result + (field375?.contentDeepHashCode() ?: 0)
        result = 31 * result + (samples?.contentHashCode() ?: 0)
        result = 31 * result + field377
        result = 31 * result + (field387?.contentHashCode() ?: 0)
        result = 31 * result + field371
        result = 31 * result + field391
        result = 31 * result + hasReadFloor.hashCode()
        result = 31 * result + field376
        return result
    }
}
