package xlitekt.cache.provider.vorbis

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.instrument.RawSound
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.VorbisSample_codebooks
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.VorbisSample_floors
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.VorbisSample_mappings
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.VorbisSample_residues
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
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.setData
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisBlockFlags
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisBlockSize0
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisBlockSize1
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisMapping
import kotlin.math.pow
import kotlin.math.sin

/**
 * @author Jordan Abraham
 */
@Serializable
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
    var field379: Boolean = false,
    var field376: Int = 0
) : EntryType(id) {
    fun toRawSound(var1: IntArray?): RawSound? {
        if (var1 != null && var1[0] <= 0) return null
        if (samples == null) {
            field377 = 0
            field387 = FloatArray(vorbisBlockSize1)
            samples = ByteArray(sampleCount)
            field371 = 0
            field391 = 0
        }

        while (field391 < field375!!.size) {
            if (var1 != null && var1[0] <= 0) {
                return null
            }
            val var2 = this.method1032(field391)
            if (var2 != null) {
                var var3 = field371
                var var4 = var2.size
                if (var4 > sampleCount - var3) {
                    var4 = sampleCount - var3
                }
                for (var5 in 0 until var4) {
                    var var6 = (128.0f + var2[var5] * 128.0f).toInt()
                    if (var6 and -256 != 0) {
                        var6 = var6.inv() shr 31
                    }
                    samples!![var3++] = (var6 - 128).toByte()
                }
                if (var1 != null) {
                    var1[0] -= var3 - field371
                }
                field371 = var3
                ++field391
            }
        }
        field387 = null
        val var7 = samples
        samples = null
        return RawSound(sampleRate, var7!!, start, end, field368)
    }

    private fun method1032(var1: Int): FloatArray? {
        setData(field375!![var1]!!, 0)
        readBit()
        val var2 = readBits(iLog(vorbisMapping!!.size - 1))
        val var3 = vorbisBlockFlags!![var2]
        val var4 = if (var3) vorbisBlockSize1 else vorbisBlockSize0
        var var5 = false
        var var6 = false
        if (var3) {
            var5 = readBit() != 0
            var6 = readBit() != 0
        }

        val var7 = var4 shr 1
        val var8: Int
        val var9: Int
        val var10: Int
        if (var3 && !var5) {
            var8 = (var4 shr 2) - (vorbisBlockSize0 shr 2)
            var9 = (vorbisBlockSize0 shr 2) + (var4 shr 2)
            var10 = vorbisBlockSize0 shr 1
        } else {
            var8 = 0
            var9 = var7
            var10 = var4 shr 1
        }

        val var11: Int
        val var12: Int
        val var13: Int
        if (var3 && !var6) {
            var11 = var4 - (var4 shr 2) - (vorbisBlockSize0 shr 2)
            var12 = (vorbisBlockSize0 shr 2) + (var4 - (var4 shr 2))
            var13 = vorbisBlockSize0 shr 1
        } else {
            var11 = var7
            var12 = var4
            var13 = var4 shr 1
        }

        val var14 = VorbisSample_mappings!![vorbisMapping!![var2]]!!
        val var16 = var14.mappingMux
        var var17 = var14.submapFloor!![var16]
        val var15 = !VorbisSample_floors!![var17]!!.readSubmapFloor()

        var17 = 0
        while (var17 < var14.submaps) {
            val var42 = VorbisSample_residues!![var14.submapResidue!![var17]]!!
            val var44 = field380!!
            var42.method834(var44, var4 shr 1, var15)
            ++var17
        }

        var var18: Int
        if (!var15) {
            var17 = var14.mappingMux
            var18 = var14.submapFloor!![var17]
            VorbisSample_floors!![var18]!!.method728(field380, var4 shr 1)
        }

        var var19: Int
        if (var15) {
            var17 = var4 shr 1
            while (var17 < var4) {
                field380!![var17] = 0.0f
                ++var17
            }
        } else {
            var17 = var4 shr 1
            var18 = var4 shr 2
            var19 = var4 shr 3
            val var20: FloatArray = field380!!
            var var21 = 0
            while (var21 < var17) {
                var20[var21] *= 0.5f
                ++var21
            }
            var21 = var17
            while (var21 < var4) {
                var20[var21] = -var20[var4 - var21 - 1]
                ++var21
            }
            val var40: FloatArray = if (var3) field363!! else field381!!
            val var22: FloatArray = if (var3) field378!! else field382!!
            val var23: FloatArray = if (var3) field367!! else field383!!
            val var24: IntArray = if (var3) field384!! else field358!!
            var var26: Float
            var var27: Float
            var var28: Float
            var var29: Float
            var var25 = 0
            while (var25 < var18) {
                var26 = var20[var25 * 4] - var20[var4 - var25 * 4 - 1]
                var27 = var20[var25 * 4 + 2] - var20[var4 - var25 * 4 - 3]
                var28 = var40[var25 * 2]
                var29 = var40[var25 * 2 + 1]
                var20[var4 - var25 * 4 - 1] = var26 * var28 - var27 * var29
                var20[var4 - var25 * 4 - 3] = var26 * var29 + var27 * var28
                ++var25
            }
            var var30: Float
            var var31: Float
            var25 = 0
            while (var25 < var19) {
                var26 = var20[var17 + var25 * 4 + 3]
                var27 = var20[var17 + var25 * 4 + 1]
                var28 = var20[var25 * 4 + 3]
                var29 = var20[var25 * 4 + 1]
                var20[var17 + var25 * 4 + 3] = var26 + var28
                var20[var17 + var25 * 4 + 1] = var27 + var29
                var30 = var40[var17 - 4 - var25 * 4]
                var31 = var40[var17 - 3 - var25 * 4]
                var20[var25 * 4 + 3] = (var26 - var28) * var30 - (var27 - var29) * var31
                var20[var25 * 4 + 1] = (var27 - var29) * var30 + (var26 - var28) * var31
                ++var25
            }
            var25 = iLog(var4 - 1)
            var var47: Int
            var var48: Int
            var var49: Int
            var var46 = 0
            while (var46 < var25 - 3) {
                var47 = var4 shr var46 + 2
                var48 = 8 shl var46
                var49 = 0
                while (var49 < 2 shl var46) {
                    val var50 = var4 - var47 * var49 * 2
                    val var51 = var4 - var47 * (var49 * 2 + 1)
                    for (var32 in 0 until (var4 shr var46 + 4)) {
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
                    ++var49
                }
                ++var46
            }
            var46 = 1
            while (var46 < var19 - 1) {
                var47 = var24[var46]
                if (var46 < var47) {
                    var48 = var46 * 8
                    var49 = var47 * 8
                    var30 = var20[var48 + 1]
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
                ++var46
            }
            var46 = 0
            while (var46 < var17) {
                var20[var46] = var20[var46 * 2 + 1]
                ++var46
            }
            var46 = 0
            while (var46 < var19) {
                var20[var4 - 1 - var46 * 2] = var20[var46 * 4]
                var20[var4 - 2 - var46 * 2] = var20[var46 * 4 + 1]
                var20[var4 - var18 - 1 - var46 * 2] = var20[var46 * 4 + 2]
                var20[var4 - var18 - 2 - var46 * 2] = var20[var46 * 4 + 3]
                ++var46
            }
            var46 = 0
            while (var46 < var19) {
                var27 = var23[var46 * 2]
                var28 = var23[var46 * 2 + 1]
                var29 = var20[var17 + var46 * 2]
                var30 = var20[var17 + var46 * 2 + 1]
                var31 = var20[var4 - 2 - var46 * 2]
                val var52 = var20[var4 - 1 - var46 * 2]
                var var53 = var28 * (var29 - var31) + var27 * (var30 + var52)
                var20[var17 + var46 * 2] = (var29 + var31 + var53) * 0.5f
                var20[var4 - 2 - var46 * 2] = (var29 + var31 - var53) * 0.5f
                var53 = var28 * (var30 + var52) - var27 * (var29 - var31)
                var20[var17 + var46 * 2 + 1] = (var30 - var52 + var53) * 0.5f
                var20[var4 - 1 - var46 * 2] = (-var30 + var52 + var53) * 0.5f
                ++var46
            }
            var46 = 0
            while (var46 < var18) {
                var20[var46] = var20[var17 + var46 * 2] * var22[var46 * 2] + var20[var17 + var46 * 2 + 1] * var22[var46 * 2 + 1]
                var20[var17 - 1 - var46] = var20[var17 + var46 * 2] * var22[var46 * 2 + 1] - var20[var17 + var46 * 2 + 1] * var22[var46 * 2]
                ++var46
            }
            var46 = 0
            while (var46 < var18) {
                var20[var46 + (var4 - var18)] = -var20[var46]
                ++var46
            }
            var46 = 0
            while (var46 < var18) {
                var20[var46] = var20[var18 + var46]
                ++var46
            }
            var46 = 0
            while (var46 < var18) {
                var20[var18 + var46] = -var20[var18 - var46 - 1]
                ++var46
            }
            var46 = 0
            while (var46 < var18) {
                var20[var17 + var46] = var20[var4 - var46 - 1]
                ++var46
            }
            var var10000: FloatArray
            var46 = var8
            while (var46 < var9) {
                var27 = sin(((var46 - var8).toDouble() + 0.5) / var10.toDouble() * 0.5 * 3.141592653589793).toFloat()
                var10000 = field380!!
                var10000[var46] *= sin(1.5707963267948966 * var27.toDouble() * var27.toDouble()).toFloat()
                ++var46
            }
            var46 = var11
            while (var46 < var12) {
                var27 = sin(((var46 - var11).toDouble() + 0.5) / var13.toDouble() * 0.5 * 3.141592653589793 + 1.5707963267948966).toFloat()
                var10000 = field380!!
                var10000[var46] *= sin(1.5707963267948966 * var27.toDouble() * var27.toDouble()).toFloat()
                ++var46
            }
        }

        var var41: FloatArray? = null
        if (field377 > 0) {
            var18 = var4 + field377 shr 2
            var41 = FloatArray(var18)
            var var43: Int
            if (!field379) {
                var19 = 0
                while (var19 < this.field376) {
                    var43 = var19 + (field377 shr 1)
                    var41[var19] += field387!![var43]
                    ++var19
                }
            }
            if (!var15) {
                var19 = var8
                while (var19 < var4 shr 1) {
                    var43 = var41.size - (var4 shr 1) + var19
                    var41[var43] += field380!![var19]
                    ++var19
                }
            }
        }

        val var54 = field387
        this.field387 = field380
        field380 = var54
        field377 = var4
        this.field376 = var12 - (var4 shr 1)
        this.field379 = var15
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
        if (field379 != other.field379) return false
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
        result = 31 * result + field379.hashCode()
        result = 31 * result + field376
        return result
    }
}

data class VorbisCodebook(
    var dimensions: Int = 0,
    var entries: Int = 0,
    var lengthMap: IntArray? = null,
    var field355: IntArray? = null,
    var field356: Array<FloatArray?>? = null,
    var keys: IntArray? = null
) {
    init {
        readBits(24)
        dimensions = readBits(16)
        entries = readBits(24)
        lengthMap = IntArray(entries)
        val var1 = readBit() != 0
        var var2: Int
        var var3: Int
        var var5: Int
        if (var1) {
            var2 = 0
            var3 = readBits(5) + 1
            while (var2 < entries) {
                val var4 = readBits(iLog(entries - var2))
                var5 = 0
                while (var5 < var4) {
                    lengthMap!![var2++] = var3
                    ++var5
                }
                ++var3
            }
        } else {
            val var14 = readBit() != 0
            var3 = 0
            while (var3 < entries) {
                if (var14 && readBit() == 0) {
                    lengthMap!![var3] = 0
                } else {
                    lengthMap!![var3] = readBits(5) + 1
                }
                ++var3
            }
        }

        method1012()
        var2 = readBits(4)
        if (var2 > 0) {
            val var15 = float32Unpack(readBits(32))
            val var16 = float32Unpack(readBits(32))
            var5 = readBits(4) + 1
            val var6 = readBit() != 0
            val var7 = if (var2 == 1) {
                mapType1QuantValues(entries, dimensions)
            } else {
                entries * dimensions
            }
            field355 = IntArray(var7)

            repeat(var7) {
                field355!![it] = readBits(var5)
            }

            field356 = Array(entries) { FloatArray(dimensions) }

            var var9: Float
            var var10: Int
            var var11: Int
            var var8: Int
            if (var2 == 1) {
                var8 = 0
                while (var8 < entries) {
                    var9 = 0.0f
                    var10 = 1
                    var11 = 0
                    while (var11 < dimensions) {
                        val var12 = var8 / var10 % var7
                        val var13 = field355!![var12].toFloat() * var16 + var15 + var9
                        field356!![var8]!![var11] = var13
                        if (var6) {
                            var9 = var13
                        }
                        var10 *= var7
                        ++var11
                    }
                    ++var8
                }
            } else {
                var8 = 0
                while (var8 < entries) {
                    var9 = 0.0f
                    var10 = var8 * dimensions
                    var11 = 0
                    while (var11 < dimensions) {
                        val var17 = field355!![var10].toFloat() * var16 + var15 + var9
                        field356!![var8]!![var11] = var17
                        if (var6) {
                            var9 = var17
                        }
                        ++var10
                        ++var11
                    }
                    ++var8
                }
            }
        }
    }

    private fun method1012() {
        val var1 = IntArray(entries)
        val var2 = IntArray(33)

        var var4: Int
        var var5: Int
        var var6: Int
        var var7: Int
        var var8: Int
        var var10: Int
        var var3 = 0
        while (var3 < entries) {
            var4 = lengthMap!![var3]
            if (var4 != 0) {
                var5 = 1 shl 32 - var4
                var6 = var2[var4]
                var1[var3] = var6
                var var12: Int
                if (var6 and var5 != 0) {
                    var7 = var2[var4 - 1]
                } else {
                    var7 = var6 or var5
                    var8 = var4 - 1
                    while (var8 >= 1) {
                        var12 = var2[var8]
                        if (var12 != var6) {
                            break
                        }
                        var10 = 1 shl 32 - var8
                        if (var12 and var10 != 0) {
                            var2[var8] = var2[var8 - 1]
                            break
                        }
                        var2[var8] = var12 or var10
                        --var8
                    }
                }
                var2[var4] = var7
                var8 = var4 + 1
                while (var8 <= 32) {
                    var12 = var2[var8]
                    if (var12 == var6) {
                        var2[var8] = var7
                    }
                    ++var8
                }
            }
            ++var3
        }

        keys = IntArray(8)
        var var11 = 0
        var3 = 0
        while (var3 < entries) {
            var4 = lengthMap!![var3]
            if (var4 != 0) {
                var5 = var1[var3]
                var6 = 0
                var7 = 0
                while (var7 < var4) {
                    var8 = Int.MIN_VALUE ushr var7
                    if (var5 and var8 != 0) {
                        if (keys!![var6] == 0) {
                            keys!![var6] = var11
                        }
                        var6 = keys!![var6]
                    } else {
                        ++var6
                    }
                    if (var6 >= keys!!.size) {
                        val var9 = IntArray(keys!!.size * 2)
                        var10 = 0
                        while (var10 < keys!!.size) {
                            var9[var10] = keys!![var10]
                            ++var10
                        }
                        keys = var9
                    }
                    var8 = var8 ushr 1
                    ++var7
                }
                keys!![var6] = var3.inv()
                if (var6 >= var11) {
                    var11 = var6 + 1
                }
            }
            ++var3
        }
    }

    private fun float32Unpack(var0: Int): Float {
        var var1 = var0 and 2097151
        val var2 = var0 and Int.MIN_VALUE
        val var3 = (var0 and 2145386496) shr 21
        if (var2 != 0) {
            var1 = -var1
        }
        return (var1.toDouble() * 2.0.pow((var3 - 788).toDouble())).toFloat()
    }

    private fun mapType1QuantValues(var0: Int, var1: Int): Int {
        var var2 = var0.toDouble().pow(1.0 / var1.toDouble()).toInt() + 1
        while (true) {
            var var4 = var2
            var var5 = var1
            var var6 = 1
            while (var5 > 1) {
                if (var5 and 1 != 0) {
                    var6 *= var4
                }
                var4 *= var4
                var5 = var5 shr 1
            }
            val var3 = if (var5 == 1) {
                var4 * var6
            } else {
                var6
            }
            if (var3 <= var0) {
                return var2
            }
            --var2
        }
    }

    fun method1013(): Int {
        var var1 = 0
        while (keys!![var1] >= 0) {
            var1 = if (readBit() != 0) keys!![var1] else var1 + 1
        }
        return keys!![var1].inv()
    }

    fun method1014(): FloatArray {
        return field356!![method1013()]!!
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VorbisCodebook

        if (dimensions != other.dimensions) return false
        if (entries != other.entries) return false
        if (lengthMap != null) {
            if (other.lengthMap == null) return false
            if (!lengthMap.contentEquals(other.lengthMap)) return false
        } else if (other.lengthMap != null) return false
        if (field355 != null) {
            if (other.field355 == null) return false
            if (!field355.contentEquals(other.field355)) return false
        } else if (other.field355 != null) return false
        if (field356 != null) {
            if (other.field356 == null) return false
            if (!field356.contentDeepEquals(other.field356)) return false
        } else if (other.field356 != null) return false
        if (keys != null) {
            if (other.keys == null) return false
            if (!keys.contentEquals(other.keys)) return false
        } else if (other.keys != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = dimensions
        result = 31 * result + entries
        result = 31 * result + (lengthMap?.contentHashCode() ?: 0)
        result = 31 * result + (field355?.contentHashCode() ?: 0)
        result = 31 * result + (field356?.contentDeepHashCode() ?: 0)
        result = 31 * result + (keys?.contentHashCode() ?: 0)
        return result
    }
}
data class VorbisFloor(
    var field268: IntArray? = null,
    var multiplier: Int = 0,
    var partitionClassList: IntArray? = null,
    var classDimensions: IntArray? = null,
    var classSubClasses: IntArray? = null,
    var classMasterbooks: IntArray? = null,
    var subclassBooks: Array<IntArray?>? = null
) {
    init {
        val var1 = readBits(16)
        if (var1 != 1) throw IllegalArgumentException("Not 1. Was $var1.")
        val var2 = readBits(5)
        var var3 = 0
        partitionClassList = IntArray(var2)

        var var5: Int
        var var4 = 0
        while (var4 < var2) {
            var5 = readBits(4)
            partitionClassList!![var4] = var5
            if (var5 >= var3) {
                var3 = var5 + 1
            }
            ++var4
        }

        classDimensions = IntArray(var3)
        classSubClasses = IntArray(var3)
        classMasterbooks = IntArray(var3)
        subclassBooks = arrayOfNulls(var3)

        var4 = 0
        while (var4 < var3) {
            classDimensions!![var4] = readBits(3) + 1
            classSubClasses!![var4] = readBits(2)
            var5 = classSubClasses!![var4]
            if (var5 != 0) {
                classMasterbooks!![var4] = readBits(8)
            }
            var5 = 1 shl var5
            val var9 = IntArray(var5)
            subclassBooks!![var4] = var9
            var var7 = 0
            while (var7 < var5) {
                var9[var7] = readBits(8) - 1
                ++var7
            }
            ++var4
        }

        multiplier = readBits(2) + 1
        var4 = readBits(4)
        var5 = 2

        var var6 = 0
        while (var6 < var2) {
            var5 += classDimensions!![partitionClassList!![var6]]
            ++var6
        }

        field268 = IntArray(var5)
        field268!![0] = 0
        field268!![1] = 1 shl var4
        var5 = 2

        var6 = 0
        while (var6 < var2) {
            val var7 = partitionClassList!![var6]
            for (var8 in 0 until classDimensions!![var7]) {
                field268!![var5++] = readBits(var4)
            }
            ++var6
        }

        if (field279 == null || field279!!.size < var5) {
            field279 = IntArray(var5)
            field278 = IntArray(var5)
            field277 = BooleanArray(var5)
        }
    }

    fun readSubmapFloor(): Boolean {
        val var1 = readBit() != 0
        return if (!var1) {
            false
        } else {
            val var2 = field268!!.size
            var var3 = 0
            while (var3 < var2) {
                field279!![var3] = field268!![var3]
                ++var3
            }
            var3 = field274[multiplier - 1]
            val var4 = iLog(var3 - 1)
            field278!![0] = readBits(var4)
            field278!![1] = readBits(var4)
            var var5 = 2
            for (var6 in partitionClassList!!.indices) {
                val var7 = partitionClassList!![var6]
                val var8 = classDimensions!![var7]
                val var9 = classSubClasses!![var7]
                val var10 = (1 shl var9) - 1
                var var11 = 0
                if (var9 > 0) {
                    var11 = VorbisSample_codebooks!![classMasterbooks!![var7]]!!.method1013()
                }
                for (var12 in 0 until var8) {
                    val var13 = subclassBooks!![var7]!![var11 and var10]
                    var11 = var11 ushr var9
                    field278!![var5++] = if (var13 >= 0) VorbisSample_codebooks!![var13]!!.method1013() else 0
                }
            }
            true
        }
    }

    fun method728(var1: FloatArray?, var2: Int) {
        val var3 = field268!!.size
        val var4 = field274[multiplier - 1]
        val var5 = field277
        field277!![1] = true
        var5!![0] = true

        var var6: Int
        var var7: Int
        var var8: Int
        var var9: Int
        var var10: Int
        var6 = 2
        while (var6 < var3) {
            var7 = method722(field279, var6)
            var8 = method721(field279, var6)
            var9 = method726(field279!![var7], field278!![var7], field279!![var8], field278!![var8], field279!![var6])
            var10 = field278!![var6]
            val var11 = var4 - var9
            val var13 = (if (var11 < var9) var11 else var9) shl 1
            if (var10 != 0) {
                val var14 = field277
                field277!![var8] = true
                var14!![var7] = true
                field277!![var6] = true
                if (var10 >= var13) {
                    field278!![var6] = if (var11 > var9) var9 + (var10 - var9) else var11 + (var9 - var10) - 1
                } else {
                    field278!![var6] = if (var10 and 1 != 0) var9 - (var10 + 1) / 2 else var10 / 2 + var9
                }
            } else {
                field277!![var6] = false
                field278!![var6] = var9
            }
            ++var6
        }

        sort(0, var3 - 1)
        var6 = 0
        var7 = field278!![0] * multiplier

        var8 = 1
        while (var8 < var3) {
            if (field277!![var8]) {
                var9 = field279!![var8]
                var10 = field278!![var8] * multiplier
                method723(var6, var7, var9, var10, var1!!, var2)
                if (var9 >= var2) {
                    return
                }
                var6 = var9
                var7 = var10
            }
            ++var8
        }

        val var16 = decibelStats[var7]

        var9 = var6
        while (var9 < var2) {
            var1!![var9] *= var16
            ++var9
        }
    }

    private fun method722(var0: IntArray?, var1: Int): Int {
        val var2 = var0!![var1]
        var var3 = -1
        var var4 = Int.MIN_VALUE
        for (var5 in 0 until var1) {
            val var6 = var0[var5]
            if (var6 in (var4 + 1) until var2) {
                var3 = var5
                var4 = var6
            }
        }
        return var3
    }

    private fun method721(var0: IntArray?, var1: Int): Int {
        val var2 = var0!![var1]
        var var3 = -1
        var var4 = Int.MAX_VALUE
        for (var5 in 0 until var1) {
            val var6 = var0[var5]
            if (var6 in (var2 + 1) until var4) {
                var3 = var5
                var4 = var6
            }
        }
        return var3
    }

    private fun method726(var1: Int, var2: Int, var3: Int, var4: Int, var5: Int): Int {
        val var6 = var4 - var2
        val var7 = var3 - var1
        val var8 = if (var6 < 0) -var6 else var6
        val var9 = (var5 - var1) * var8
        val var10 = var9 / var7
        return if (var6 < 0) var2 - var10 else var10 + var2
    }

    private fun sort(var1: Int, var2: Int) {
        if (var1 < var2) {
            var var3 = var1
            val var4 = field279!![var1]
            val var5 = field278!![var1]
            val var6 = field277!![var1]
            for (var7 in var1 + 1..var2) {
                val var8 = field279!![var7]
                if (var8 < var4) {
                    field279!![var3] = var8
                    field278!![var3] = field278!![var7]
                    field277!![var3] = field277!![var7]
                    ++var3
                    field279!![var7] = field279!![var3]
                    field278!![var7] = field278!![var3]
                    field277!![var7] = field277!![var3]
                }
            }
            field279!![var3] = var4
            field278!![var3] = var5
            field277!![var3] = var6
            sort(var1, var3 - 1)
            sort(var3 + 1, var2)
        }
    }

    private fun method723(var1: Int, var2: Int, i: Int, var4: Int, var5: FloatArray, var6: Int) {
        var var3 = i
        val var7 = var4 - var2
        val var8 = var3 - var1
        var var9 = if (var7 < 0) -var7 else var7
        val var10 = var7 / var8
        var var11 = var2
        var var12 = 0
        val var13 = if (var7 < 0) var10 - 1 else var10 + 1
        var9 -= (if (var10 < 0) -var10 else var10) * var8
        var5[var1] *= decibelStats[var2]
        if (var3 > var6) {
            var3 = var6
        }
        for (var14 in var1 + 1 until var3) {
            var12 += var9
            if (var12 >= var8) {
                var12 -= var8
                var11 += var13
            } else {
                var11 += var10
            }
            var5[var14] *= decibelStats[var11]
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VorbisFloor

        if (field268 != null) {
            if (other.field268 == null) return false
            if (!field268.contentEquals(other.field268)) return false
        } else if (other.field268 != null) return false
        if (multiplier != other.multiplier) return false
        if (partitionClassList != null) {
            if (other.partitionClassList == null) return false
            if (!partitionClassList.contentEquals(other.partitionClassList)) return false
        } else if (other.partitionClassList != null) return false
        if (classDimensions != null) {
            if (other.classDimensions == null) return false
            if (!classDimensions.contentEquals(other.classDimensions)) return false
        } else if (other.classDimensions != null) return false
        if (classSubClasses != null) {
            if (other.classSubClasses == null) return false
            if (!classSubClasses.contentEquals(other.classSubClasses)) return false
        } else if (other.classSubClasses != null) return false
        if (classMasterbooks != null) {
            if (other.classMasterbooks == null) return false
            if (!classMasterbooks.contentEquals(other.classMasterbooks)) return false
        } else if (other.classMasterbooks != null) return false
        if (subclassBooks != null) {
            if (other.subclassBooks == null) return false
            if (!subclassBooks.contentDeepEquals(other.subclassBooks)) return false
        } else if (other.subclassBooks != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = field268?.contentHashCode() ?: 0
        result = 31 * result + multiplier
        result = 31 * result + (partitionClassList?.contentHashCode() ?: 0)
        result = 31 * result + (classDimensions?.contentHashCode() ?: 0)
        result = 31 * result + (classSubClasses?.contentHashCode() ?: 0)
        result = 31 * result + (classMasterbooks?.contentHashCode() ?: 0)
        result = 31 * result + (subclassBooks?.contentDeepHashCode() ?: 0)
        return result
    }

    internal companion object {
        var field279: IntArray? = null
        var field278: IntArray? = null
        var field277: BooleanArray? = null
        val field274: IntArray = intArrayOf(256, 128, 86, 64)
        val decibelStats = floatArrayOf(
            1.0649863E-7f,
            1.1341951E-7f,
            1.2079015E-7f,
            1.2863978E-7f,
            1.369995E-7f,
            1.459025E-7f,
            1.5538409E-7f,
            1.6548181E-7f,
            1.7623574E-7f,
            1.8768856E-7f,
            1.998856E-7f,
            2.128753E-7f,
            2.2670913E-7f,
            2.4144197E-7f,
            2.5713223E-7f,
            2.7384212E-7f,
            2.9163792E-7f,
            3.1059022E-7f,
            3.307741E-7f,
            3.5226967E-7f,
            3.7516213E-7f,
            3.995423E-7f,
            4.255068E-7f,
            4.5315863E-7f,
            4.8260745E-7f,
            5.1397E-7f,
            5.4737063E-7f,
            5.829419E-7f,
            6.208247E-7f,
            6.611694E-7f,
            7.041359E-7f,
            7.4989464E-7f,
            7.98627E-7f,
            8.505263E-7f,
            9.057983E-7f,
            9.646621E-7f,
            1.0273513E-6f,
            1.0941144E-6f,
            1.1652161E-6f,
            1.2409384E-6f,
            1.3215816E-6f,
            1.4074654E-6f,
            1.4989305E-6f,
            1.5963394E-6f,
            1.7000785E-6f,
            1.8105592E-6f,
            1.9282195E-6f,
            2.053526E-6f,
            2.1869757E-6f,
            2.3290977E-6f,
            2.4804558E-6f,
            2.6416496E-6f,
            2.813319E-6f,
            2.9961443E-6f,
            3.1908505E-6f,
            3.39821E-6f,
            3.619045E-6f,
            3.8542307E-6f,
            4.1047006E-6f,
            4.371447E-6f,
            4.6555283E-6f,
            4.958071E-6f,
            5.280274E-6f,
            5.623416E-6f,
            5.988857E-6f,
            6.3780467E-6f,
            6.7925284E-6f,
            7.2339453E-6f,
            7.704048E-6f,
            8.2047E-6f,
            8.737888E-6f,
            9.305725E-6f,
            9.910464E-6f,
            1.0554501E-5f,
            1.1240392E-5f,
            1.1970856E-5f,
            1.2748789E-5f,
            1.3577278E-5f,
            1.4459606E-5f,
            1.5399271E-5f,
            1.6400005E-5f,
            1.7465769E-5f,
            1.8600793E-5f,
            1.9809577E-5f,
            2.1096914E-5f,
            2.2467912E-5f,
            2.3928002E-5f,
            2.5482977E-5f,
            2.7139005E-5f,
            2.890265E-5f,
            3.078091E-5f,
            3.2781227E-5f,
            3.4911533E-5f,
            3.718028E-5f,
            3.9596467E-5f,
            4.2169668E-5f,
            4.491009E-5f,
            4.7828602E-5f,
            5.0936775E-5f,
            5.424693E-5f,
            5.7772202E-5f,
            6.152657E-5f,
            6.552491E-5f,
            6.9783084E-5f,
            7.4317984E-5f,
            7.914758E-5f,
            8.429104E-5f,
            8.976875E-5f,
            9.560242E-5f,
            1.0181521E-4f,
            1.0843174E-4f,
            1.1547824E-4f,
            1.2298267E-4f,
            1.3097477E-4f,
            1.3948625E-4f,
            1.4855085E-4f,
            1.5820454E-4f,
            1.6848555E-4f,
            1.7943469E-4f,
            1.9109536E-4f,
            2.0351382E-4f,
            2.167393E-4f,
            2.3082423E-4f,
            2.4582449E-4f,
            2.6179955E-4f,
            2.7881275E-4f,
            2.9693157E-4f,
            3.1622787E-4f,
            3.3677815E-4f,
            3.5866388E-4f,
            3.8197188E-4f,
            4.0679457E-4f,
            4.3323037E-4f,
            4.613841E-4f,
            4.913675E-4f,
            5.2329927E-4f,
            5.573062E-4f,
            5.935231E-4f,
            6.320936E-4f,
            6.731706E-4f,
            7.16917E-4f,
            7.635063E-4f,
            8.1312325E-4f,
            8.6596457E-4f,
            9.2223985E-4f,
            9.821722E-4f,
            0.0010459992f,
            0.0011139743f,
            0.0011863665f,
            0.0012634633f,
            0.0013455702f,
            0.0014330129f,
            0.0015261382f,
            0.0016253153f,
            0.0017309374f,
            0.0018434235f,
            0.0019632196f,
            0.0020908006f,
            0.0022266726f,
            0.0023713743f,
            0.0025254795f,
            0.0026895993f,
            0.0028643848f,
            0.0030505287f,
            0.003248769f,
            0.0034598925f,
            0.0036847359f,
            0.0039241905f,
            0.0041792067f,
            0.004450795f,
            0.004740033f,
            0.005048067f,
            0.0053761187f,
            0.005725489f,
            0.0060975635f,
            0.0064938175f,
            0.0069158226f,
            0.0073652514f,
            0.007843887f,
            0.008353627f,
            0.008896492f,
            0.009474637f,
            0.010090352f,
            0.01074608f,
            0.011444421f,
            0.012188144f,
            0.012980198f,
            0.013823725f,
            0.014722068f,
            0.015678791f,
            0.016697686f,
            0.017782796f,
            0.018938422f,
            0.020169148f,
            0.021479854f,
            0.022875736f,
            0.02436233f,
            0.025945531f,
            0.027631618f,
            0.029427277f,
            0.031339627f,
            0.03337625f,
            0.035545226f,
            0.037855156f,
            0.0403152f,
            0.042935107f,
            0.045725275f,
            0.048696756f,
            0.05186135f,
            0.05523159f,
            0.05882085f,
            0.062643364f,
            0.06671428f,
            0.07104975f,
            0.075666964f,
            0.08058423f,
            0.08582105f,
            0.09139818f,
            0.097337745f,
            0.1036633f,
            0.11039993f,
            0.11757434f,
            0.12521498f,
            0.13335215f,
            0.14201812f,
            0.15124726f,
            0.16107617f,
            0.1715438f,
            0.18269168f,
            0.19456401f,
            0.20720787f,
            0.22067343f,
            0.23501402f,
            0.25028655f,
            0.26655158f,
            0.28387362f,
            0.3023213f,
            0.32196787f,
            0.34289113f,
            0.36517414f,
            0.3889052f,
            0.41417846f,
            0.44109413f,
            0.4697589f,
            0.50028646f,
            0.53279793f,
            0.5674221f,
            0.6042964f,
            0.64356697f,
            0.6853896f,
            0.72993004f,
            0.777365f,
            0.8278826f,
            0.88168305f,
            0.9389798f,
            1.0f
        )
    }
}

data class VorbisResidue(
    var residueType: Int = 0,
    var begin: Int = 0,
    var end: Int = 0,
    var partitionSize: Int = 0,
    var classifications: Int = 0,
    var classbook: Int = 0,
    var cascade: IntArray? = null
) {
    init {
        residueType = readBits(16)
        begin = readBits(24)
        end = readBits(24)
        partitionSize = readBits(24) + 1
        classifications = readBits(6) + 1
        classbook = readBits(8)
        val var1 = IntArray(classifications)
        var var2 = 0
        while (var2 < classifications) {
            var var3 = 0
            val var4 = readBits(3)
            val var5 = readBit() != 0
            if (var5) {
                var3 = readBits(5)
            }
            var1[var2] = var3 shl 3 or var4
            ++var2
        }

        cascade = IntArray(classifications * 8)

        var2 = 0
        while (var2 < classifications * 8) {
            cascade!![var2] = if (var1[var2 shr 3] and (1 shl (var2 and 7)) != 0) readBits(8) else -1
            ++var2
        }
    }

    fun method834(var1: FloatArray, var2: Int, var3: Boolean) {
        var var4: Int
        var4 = 0
        while (var4 < var2) {
            var1[var4] = 0.0f
            ++var4
        }

        if (!var3) {
            var4 = VorbisSample_codebooks!![classbook]!!.dimensions
            val var5 = end - begin
            val var6 = var5 / partitionSize
            val var7 = IntArray(var6)
            for (var8 in 0..7) {
                var var9 = 0
                while (var9 < var6) {
                    var var10: Int
                    var var11: Int
                    if (var8 == 0) {
                        var10 = VorbisSample_codebooks!![classbook]!!.method1013()
                        var11 = var4 - 1
                        while (var11 >= 0) {
                            if (var9 + var11 < var6) {
                                var7[var9 + var11] = var10 % classifications
                            }
                            var10 /= classifications
                            --var11
                        }
                    }
                    var10 = 0
                    while (var10 < var4) {
                        var11 = var7[var9]
                        val var12 = cascade!![var8 + var11 * 8]
                        if (var12 >= 0) {
                            val var13 = var9 * partitionSize + begin
                            val var14 = VorbisSample_codebooks!![var12]
                            var var15: Int
                            if (residueType == 0) {
                                var15 = partitionSize / var14!!.dimensions
                                for (var19 in 0 until var15) {
                                    val var20 = var14.method1014()
                                    for (var18 in 0 until var14.dimensions) {
                                        var1[var13 + var19 + var18 * var15] += var20[var18]
                                    }
                                }
                            } else {
                                var15 = 0
                                while (var15 < partitionSize) {
                                    val var16 = var14!!.method1014()
                                    for (var17 in 0 until var14.dimensions) {
                                        var1[var13 + var15] += var16[var17]
                                        ++var15
                                    }
                                }
                            }
                        }
                        ++var9
                        if (var9 >= var6) {
                            break
                        }
                        ++var10
                    }
                }
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VorbisResidue

        if (residueType != other.residueType) return false
        if (begin != other.begin) return false
        if (end != other.end) return false
        if (partitionSize != other.partitionSize) return false
        if (classifications != other.classifications) return false
        if (classbook != other.classbook) return false
        if (cascade != null) {
            if (other.cascade == null) return false
            if (!cascade.contentEquals(other.cascade)) return false
        } else if (other.cascade != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = residueType
        result = 31 * result + begin
        result = 31 * result + end
        result = 31 * result + partitionSize
        result = 31 * result + classifications
        result = 31 * result + classbook
        result = 31 * result + (cascade?.contentHashCode() ?: 0)
        return result
    }
}

data class VorbisMapping(
    var submaps: Int = 0,
    var mappingMux: Int = 0,
    var submapFloor: IntArray? = null,
    var submapResidue: IntArray? = null
) {
    init {
        readBits(16)
        val i = readBit() != 0
        submaps = if (i) readBits(4) + 1 else 1
        if (readBit() != 0) {
            readBits(8)
        }
        readBits(2)
        if (submaps > 1) {
            mappingMux = readBits(4)
        }
        submapFloor = IntArray(submaps)
        submapResidue = IntArray(submaps)
        repeat(submaps) {
            readBits(8)
            submapFloor!![it] = readBits(8)
            submapResidue!![it] = readBits(8)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VorbisMapping

        if (submaps != other.submaps) return false
        if (mappingMux != other.mappingMux) return false
        if (submapFloor != null) {
            if (other.submapFloor == null) return false
            if (!submapFloor.contentEquals(other.submapFloor)) return false
        } else if (other.submapFloor != null) return false
        if (submapResidue != null) {
            if (other.submapResidue == null) return false
            if (!submapResidue.contentEquals(other.submapResidue)) return false
        } else if (other.submapResidue != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = submaps
        result = 31 * result + mappingMux
        result = 31 * result + (submapFloor?.contentHashCode() ?: 0)
        result = 31 * result + (submapResidue?.contentHashCode() ?: 0)
        return result
    }
}
