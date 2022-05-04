package xlitekt.cache.provider.vorbis

import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.iLog
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBit
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.readBits
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider.Companion.vorbisCodebooks

/**
 * @author Jordan Abraham
 */
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
        val partitionSize = readBits(5)
        var subSize = 0
        partitionClassList = IntArray(partitionSize)

        repeat(partitionSize) {
            val var5 = readBits(4)
            partitionClassList!![it] = var5
            if (var5 >= subSize) {
                subSize = var5 + 1
            }
        }

        classDimensions = IntArray(subSize)
        classSubClasses = IntArray(subSize)
        classMasterbooks = IntArray(subSize)
        subclassBooks = arrayOfNulls(subSize)

        repeat(subSize) {
            classDimensions!![it] = readBits(3) + 1
            classSubClasses!![it] = readBits(2)
            var var5 = classSubClasses!![it]
            if (var5 != 0) {
                classMasterbooks!![it] = readBits(8)
            }
            var5 = 1 shl var5
            val var9 = IntArray(var5)
            subclassBooks!![it] = var9
            repeat(var5) { var7 ->
                var9[var7] = readBits(8) - 1
            }
        }

        multiplier = readBits(2) + 1
        val var4 = readBits(4)
        var var5 = 2

        repeat(partitionSize) {
            var5 += classDimensions!![partitionClassList!![it]]
        }

        field268 = IntArray(var5)
        field268!![0] = 0
        field268!![1] = 1 shl var4
        var5 = 2

        repeat(partitionSize) {
            val var7 = partitionClassList!![it]
            repeat(classDimensions!![var7]) {
                field268!![var5++] = readBits(var4)
            }
        }
        if (field279 == null || field279!!.size < var5) {
            field279 = IntArray(var5)
            field278 = IntArray(var5)
            field277 = BooleanArray(var5)
        }
    }

    fun readSubmapFloor(): Boolean {
        val hasRead = readBit() != 0
        return if (!hasRead) {
            false
        } else {
            repeat(field268!!.size) {
                field279!![it] = field268!![it]
            }
            val var4 = iLog(field274[multiplier - 1] - 1)
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
                    var11 = vorbisCodebooks!![classMasterbooks!![var7]]!!.method1013()
                }
                repeat(var8) {
                    val var13 = subclassBooks!![var7]!![var11 and var10]
                    var11 = var11 ushr var9
                    field278!![var5++] = if (var13 >= 0) vorbisCodebooks!![var13]!!.method1013() else 0
                }
            }
            true
        }
    }

    fun method728(var1: FloatArray?, var2: Int) {
        val size = field268!!.size
        val var4 = field274[multiplier - 1]
        val var5 = field277
        field277!![1] = true
        var5!![0] = true

        var var6 = 2
        while (var6 < size) {
            val var7 = method722(field279, var6)
            val var8 = method721(field279, var6)
            val var9 = method726(field279!![var7], field278!![var7], field279!![var8], field278!![var8], field279!![var6])
            val var10 = field278!![var6]
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

        sort(0, size - 1)
        var6 = 0
        var var7 = field278!![0] * multiplier

        var var8 = 1
        while (var8 < size) {
            if (field277!![var8]) {
                val var9 = field279!![var8]
                val var10 = field278!![var8] * multiplier
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

        var var9 = var6
        while (var9 < var2) {
            var1!![var9] *= var16
            ++var9
        }
    }

    private fun method722(var0: IntArray?, var1: Int): Int {
        val var2 = var0!![var1]
        var var3 = -1
        var var4 = Int.MIN_VALUE
        repeat(var1) {
            val var6 = var0[it]
            if (var6 in (var4 + 1) until var2) {
                var3 = it
                var4 = var6
            }
        }
        return var3
    }

    private fun method721(var0: IntArray?, var1: Int): Int {
        val var2 = var0!![var1]
        var var3 = -1
        var var4 = Int.MAX_VALUE
        repeat(var1) {
            val var6 = var0[it]
            if (var6 in (var2 + 1) until var4) {
                var3 = it
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

    private fun sort(from: Int, to: Int) {
        if (from < to) {
            var startingIndex = from
            val var4 = field279!![from]
            val var5 = field278!![from]
            val var6 = field277!![from]
            for (index in from + 1..to) {
                val var8 = field279!![index]
                if (var8 < var4) {
                    field279!![startingIndex] = var8
                    field278!![startingIndex] = field278!![index]
                    field277!![startingIndex] = field277!![index]
                    ++startingIndex
                    field279!![index] = field279!![startingIndex]
                    field278!![index] = field278!![startingIndex]
                    field277!![index] = field277!![startingIndex]
                }
            }
            field279!![startingIndex] = var4
            field278!![startingIndex] = var5
            field277!![startingIndex] = var6
            sort(from, startingIndex - 1)
            sort(startingIndex + 1, to)
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
