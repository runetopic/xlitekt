package xlitekt.cache.provider.vorbis

import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBit
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBits
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisCodebooks

/**
 * @author Jordan Abraham
 */
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
        repeat(classifications) {
            var var3 = 0
            val var4 = readBits(3)
            val var5 = readBit() != 0
            if (var5) {
                var3 = readBits(5)
            }
            var1[it] = var3 shl 3 or var4
        }
        cascade = IntArray(classifications * 8)
        repeat(classifications * 8) {
            cascade!![it] = if (var1[it shr 3] and (1 shl (it and 7)) != 0) readBits(8) else -1
        }
    }

    fun method834(var1: FloatArray, var2: Int, var3: Boolean) {
        repeat(var2) {
            var1[it] = 0.0f
        }
        if (!var3) {
            val var4 = vorbisCodebooks!![classbook]!!.dimensions
            val var5 = end - begin
            val var6 = var5 / partitionSize
            val var7 = IntArray(var6)
            repeat(8) { var8 ->
                var var9 = 0
                while (var9 < var6) {
                    if (var8 == 0) {
                        var var10 = vorbisCodebooks!![classbook]!!.method1013()
                        var var11 = var4 - 1
                        while (var11 >= 0) {
                            if (var9 + var11 < var6) {
                                var7[var9 + var11] = var10 % classifications
                            }
                            var10 /= classifications
                            --var11
                        }
                    }
                    repeat(var4) {
                        val var11 = var7[var9]
                        val var12 = cascade!![var8 + var11 * 8]
                        if (var12 >= 0) {
                            val var13 = var9 * partitionSize + begin
                            val var14 = vorbisCodebooks!![var12]
                            if (residueType == 0) {
                                val var15 = partitionSize / var14!!.dimensions
                                repeat(var15) { var19 ->
                                    val var20 = var14.method1014()
                                    repeat(var14.dimensions) { var18 ->
                                        var1[var13 + var19 + var18 * var15] += var20[var18]
                                    }
                                }
                            } else {
                                var var15 = 0
                                while (var15 < partitionSize) {
                                    val var16 = var14!!.method1014()
                                    repeat(var14.dimensions) {
                                        var1[var13 + var15] += var16[it]
                                        ++var15
                                    }
                                }
                            }
                        }
                        ++var9
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
