package xlitekt.cache.provider.soundeffect

import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte
import io.ktor.utils.io.core.readUShort
import kotlin.math.cos
import kotlin.math.pow

/**
 * @author Jordan Abraham
 */
data class SoundEffectAudioFilter(
    var pairs: IntArray = IntArray(2),
    var field404: Array<Array<IntArray?>?> = Array(2) { Array(2) { IntArray(4) } },
    var field405: Array<Array<IntArray?>?> = Array(2) { Array(2) { IntArray(4) } },
    var field406: IntArray = IntArray(2)
) {
    fun method1089(buffer: ByteReadPacket, filterEnvelope: SoundEffectEnvelope) {
        val var3 = buffer.readUByte().toInt()
        pairs[0] = var3 shr 4
        pairs[1] = var3 and 15
        if (var3 != 0) {
            field406[0] = buffer.readUShort().toInt()
            field406[1] = buffer.readUShort().toInt()
            val var7 = buffer.readUByte().toInt()

            repeat(2) {
                repeat(pairs[it]) { pair ->
                    field404[it]!![0]!![pair] = buffer.readUShort().toInt()
                    field405[it]!![0]!![pair] = buffer.readUShort().toInt()
                }
            }

            repeat(2) {
                repeat(pairs[it]) { pair ->
                    if (var7 and (1 shl it * 4 shl pair) != 0) {
                        field404[it]!![1]!![pair] = buffer.readUShort().toInt()
                        field405[it]!![1]!![pair] = buffer.readUShort().toInt()
                    } else {
                        field404[it]!![1]!![pair] = field404[it]!![0]!![pair]
                        field405[it]!![1]!![pair] = field405[it]!![0]!![pair]
                    }
                }
            }

            if (var7 != 0 || field406[1] != field406[0]) {
                filterEnvelope.decodeSegments(buffer)
            }
        } else {
            val var4 = field406
            field406[1] = 0
            var4[0] = 0
        }
    }

    fun compute(var1: Int, var2: Float): Int {
        if (var1 == 0) {
            val var3 = (field406[0].toFloat() + (field406[1] - field406[0]).toFloat() * var2) * 0.0030517578f
            field409 = 0.1.pow((var3 / 20.0f).toDouble()).toFloat()
            forwardMultiplier = (field409 * 65536.0f).toInt()
        }

        return if (pairs[var1] == 0) {
            0
        } else {
            val var3 = method1097(var1, 0, var2)
            field407[var1]!![0] = -2.0f * var3 * cos(method1091(var1, 0, var2).toDouble()).toFloat()
            field407[var1]!![1] = var3 * var3
            var var10000: FloatArray
            var var4 = 1
            while (var4 < pairs[var1]) {
                val var31 = method1097(var1, var4, var2)
                val var5 = -2.0f * var31 * cos(method1091(var1, var4, var2).toDouble()).toFloat()
                val var6 = var31 * var31
                field407[var1]!![var4 * 2 + 1] = field407[var1]!![var4 * 2 - 1] * var6
                field407[var1]!![var4 * 2] = field407[var1]!![var4 * 2 - 1] * var5 + field407[var1]!![var4 * 2 - 2] * var6
                for (index in var4 * 2 - 1 downTo 2) {
                    var10000 = field407[var1]!!
                    var10000[index] += field407[var1]!![index - 1] * var5 + field407[var1]!![index - 2] * var6
                }
                var10000 = field407[var1]!!
                var10000[1] += field407[var1]!![0] * var5 + var6
                var10000 = field407[var1]!!
                var10000[0] += var5
                ++var4
            }
            if (var1 == 0) {
                repeat(pairs[0] * 2) {
                    var10000 = field407[0]!!
                    var10000[var4] *= field409
                }
            }
            repeat(pairs[var1] * 2) {
                coefficients[var1]!![var4] = (field407[var1]!![var4] * 65536.0f).toInt()
            }
            pairs[var1] * 2
        }
    }

    private fun method1097(var1: Int, var2: Int, var3: Float): Float = 1.0f - 10.0.pow((-(field405[var1]!![0]!![var2].toFloat() + var3 * (field405[var1]!![1]!![var2] - field405[var1]!![0]!![var2]).toFloat() * 0.0015258789f) / 20.0f).toDouble()).toFloat()
    private fun method1091(var1: Int, var2: Int, var3: Float): Float = normalize(field404[var1]!![0]!![var2].toFloat() + var3 * (field404[var1]!![1]!![var2] - field404[var1]!![0]!![var2]).toFloat() * 1.2207031E-4f)
    private fun normalize(var0: Float): Float = (32.703197f * 2.0.pow(var0.toDouble()).toFloat()) * 3.1415927f / 11025.0f

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundEffectAudioFilter

        if (!pairs.contentEquals(other.pairs)) return false
        if (!field404.contentDeepEquals(other.field404)) return false
        if (!field405.contentDeepEquals(other.field405)) return false
        if (!field406.contentEquals(other.field406)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = pairs.contentHashCode()
        result = 31 * result + field404.contentDeepHashCode()
        result = 31 * result + field405.contentDeepHashCode()
        result = 31 * result + field406.contentHashCode()
        return result
    }

    internal companion object {
        var forwardMultiplier: Int = 0
        var coefficients = Array<IntArray?>(2) { IntArray(8) }
        var field407 = Array<FloatArray?>(2) { FloatArray(8) }
        var field409: Float = 0f
    }
}
