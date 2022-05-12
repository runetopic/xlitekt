package xlitekt.cache.provider.vorbis

import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.iLog
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBit
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBits
import kotlin.math.pow

/**
 * @author Jordan Abraham
 */
internal data class VorbisCodebook(
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
        if (var1) {
            var var2 = 0
            var var3 = readBits(5) + 1
            while (var2 < entries) {
                repeat(readBits(iLog(entries - var2))) {
                    lengthMap!![var2++] = var3
                }
                ++var3
            }
        } else {
            val var14 = readBit() != 0
            repeat(entries) {
                when {
                    var14 && readBit() == 0 -> lengthMap!![it] = 0
                    else -> lengthMap!![it] = readBits(5) + 1
                }
            }
        }

        method1012()
        val var2 = readBits(4)
        if (var2 > 0) {
            val var15 = float32Unpack(readBits(32))
            val var16 = float32Unpack(readBits(32))
            val var5 = readBits(4) + 1
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

            if (var2 == 1) {
                repeat(entries) { var8 ->
                    var var9 = 0.0f
                    var var10 = 1
                    repeat(dimensions) {
                        val var12 = var8 / var10 % var7
                        val var13 = field355!![var12].toFloat() * var16 + var15 + var9
                        field356!![var8]!![it] = var13
                        if (var6) {
                            var9 = var13
                        }
                        var10 *= var7
                    }
                }
            } else {
                repeat(entries) { var8 ->
                    var var9 = 0.0f
                    var var10 = var8 * dimensions
                    repeat(dimensions) {
                        val var17 = field355!![var10].toFloat() * var16 + var15 + var9
                        field356!![var8]!![it] = var17
                        if (var6) {
                            var9 = var17
                        }
                        ++var10
                    }
                }
            }
        }
    }

    private fun method1012() {
        val var1 = IntArray(entries)
        val var2 = IntArray(33)

        repeat(entries) {
            val length = lengthMap!![it]
            if (length != 0) {
                val var5 = 1 shl 32 - length
                val var6 = var2[length]
                var1[it] = var6
                val var7: Int
                var var12: Int
                if (var6 and var5 != 0) {
                    var7 = var2[length - 1]
                } else {
                    var7 = var6 or var5
                    for (var8 in length - 1 downTo 1) {
                        var12 = var2[var8]
                        if (var12 != var6) {
                            break
                        }
                        val var10 = 1 shl 32 - var8
                        if (var12 and var10 != 0) {
                            var2[var8] = var2[var8 - 1]
                            break
                        }
                        var2[var8] = var12 or var10
                    }
                }
                var2[length] = var7
                for (var8 in (length + 1) until 32 + 1) {
                    var12 = var2[var8]
                    if (var12 == var6) {
                        var2[var8] = var7
                    }
                }
            }
        }

        keys = IntArray(8)
        var var11 = 0
        repeat(entries) { var3 ->
            val var4 = lengthMap!![var3]
            if (var4 != 0) {
                val var5 = var1[var3]
                var var6 = 0
                repeat(var4) { var7 ->
                    var var8 = Int.MIN_VALUE ushr var7
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
                        repeat(keys!!.size) {
                            var9[it] = keys!![it]
                        }
                        keys = var9
                    }
                    var8 = var8 ushr 1
                }
                keys!![var6] = var3.inv()
                if (var6 >= var11) {
                    var11 = var6 + 1
                }
            }
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
            val var3 = if (var5 == 1) var4 * var6 else var6
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

    fun method1014(): FloatArray = field356!![method1013()]!!

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
