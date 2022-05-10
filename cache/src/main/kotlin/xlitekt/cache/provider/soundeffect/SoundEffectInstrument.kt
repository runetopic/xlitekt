package xlitekt.cache.provider.soundeffect

import java.nio.ByteBuffer
import kotlin.math.pow
import kotlin.math.sin
import kotlin.random.Random

/**
 * @author Jordan Abraham
 */
data class SoundEffectInstrument(
    val buffer: ByteBuffer,
    var oscillatorVolume: IntArray = intArrayOf(0, 0, 0, 0, 0),
    var oscillatorPitch: IntArray = intArrayOf(0, 0, 0, 0, 0),
    var oscillatorDelays: IntArray = intArrayOf(0, 0, 0, 0, 0),
    var delayTime: Int = 0,
    var delayDecay: Int = 100,
    var duration: Int = 500,
    var offset: Int = 0,
    var pitch: SoundEffectEnvelope? = null,
    var volume: SoundEffectEnvelope? = null,
    var pitchModifier: SoundEffectEnvelope? = null,
    var pitchModifierAmplitude: SoundEffectEnvelope? = null,
    var volumeMultiplier: SoundEffectEnvelope? = null,
    var volumeMultiplierAmplitude: SoundEffectEnvelope? = null,
    var release: SoundEffectEnvelope? = null,
    var attack: SoundEffectEnvelope? = null,
    var filter: SoundEffectAudioFilter? = null,
    var filterEnvelope: SoundEffectEnvelope? = null
) {
    init {
        pitch = SoundEffectEnvelope(buffer)
        volume = SoundEffectEnvelope(buffer)
        if (buffer.get().toInt() and 0xff != 0) {
            buffer.position(buffer.position() - 1)
            pitchModifier = SoundEffectEnvelope(buffer)
            pitchModifierAmplitude = SoundEffectEnvelope(buffer)
        }
        if (buffer.get().toInt() and 0xff != 0) {
            buffer.position(buffer.position() - 1)
            volumeMultiplier = SoundEffectEnvelope(buffer)
            volumeMultiplierAmplitude = SoundEffectEnvelope(buffer)
        }
        if (buffer.get().toInt() and 0xff != 0) {
            buffer.position(buffer.position() - 1)
            release = SoundEffectEnvelope(buffer)
            attack = SoundEffectEnvelope(buffer)
        }
        for (index in 0 until 10) {
            val var4 = getUShortSmart()
            if (var4 == 0) break
            oscillatorVolume[index] = var4
            oscillatorPitch[index] = getShortSmart()
            oscillatorDelays[index] = getUShortSmart()
        }
        delayTime = getUShortSmart()
        delayDecay = getUShortSmart()
        duration = buffer.short.toInt() and 0xffff
        offset = buffer.short.toInt() and 0xffff
        filter = SoundEffectAudioFilter()
        filterEnvelope = SoundEffectEnvelope()
        filter!!.method1089(buffer, filterEnvelope!!)
    }

    private fun getUShortSmart(): Int {
        if (buffer[buffer.position()].toInt() and 0xff < 128) {
            return buffer.get().toInt() and 0xff
        }
        val value = buffer.short.toInt() and 0xffff
        return value - 32769
    }

    private fun getShortSmart(): Int {
        if (buffer[buffer.position()].toInt() and 0xff < 128) {
            return (buffer.get().toInt() and 0xff) - 64
        }
        val value = buffer.short.toInt() and 0xffff
        return value - 49152
    }

    fun synthesize(var1: Int, var2: Int): IntArray {
        clearIntArray(Instrument_samples, 0, var1)
        if (var2 < 10) return Instrument_samples
        val var3: Double = var1.toDouble() / (var2.toDouble() + 0.0)
        pitch!!.reset()
        volume!!.reset()
        var var5 = 0
        var var6 = 0
        var var7 = 0
        if (pitchModifier != null) {
            pitchModifier!!.reset()
            pitchModifierAmplitude!!.reset()
            var5 = ((pitchModifier!!.end - pitchModifier!!.start).toDouble() * 32.768 / var3).toInt()
            var6 = (pitchModifier!!.start.toDouble() * 32.768 / var3).toInt()
        }

        var var8 = 0
        var var9 = 0
        var var10 = 0
        if (volumeMultiplier != null) {
            volumeMultiplier!!.reset()
            volumeMultiplierAmplitude!!.reset()
            var8 = ((volumeMultiplier!!.end - volumeMultiplier!!.start).toDouble() * 32.768 / var3).toInt()
            var9 = (volumeMultiplier!!.start.toDouble() * 32.768 / var3).toInt()
        }

        var var11: Int
        var11 = 0
        while (var11 < 5) {
            if (oscillatorVolume[var11] != 0) {
                Instrument_phases[var11] = 0
                Instrument_delays[var11] = (oscillatorDelays[var11].toDouble() * var3).toInt()
                Instrument_volumeSteps[var11] = (oscillatorVolume[var11] shl 14) / 100
                Instrument_pitchSteps[var11] = ((pitch!!.end - pitch!!.start).toDouble() * 32.768 * 1.0057929410678534.pow(oscillatorPitch[var11].toDouble()) / var3).toInt()
                Instrument_pitchBaseSteps[var11] = (pitch!!.start.toDouble() * 32.768 / var3).toInt()
            }
            ++var11
        }

        var var12: Int
        var var13: Int
        var var14: Int
        var var15: Int
        var var10000: IntArray
        var11 = 0
        while (var11 < var1) {
            var12 = pitch!!.doStep(var1)
            var13 = volume!!.doStep(var1)
            if (pitchModifier != null) {
                var14 = pitchModifier!!.doStep(var1)
                var15 = pitchModifierAmplitude!!.doStep(var1)
                var12 += this.evaluateWave(var7, var15, pitchModifier!!.form) shr 1
                var7 = var7 + var6 + (var14 * var5 shr 16)
            }
            if (volumeMultiplier != null) {
                var14 = volumeMultiplier!!.doStep(var1)
                var15 = volumeMultiplierAmplitude!!.doStep(var1)
                var13 = var13 * ((this.evaluateWave(var10, var15, volumeMultiplier!!.form) shr 1) + 32768) shr 15
                var10 = var10 + var9 + (var14 * var8 shr 16)
            }
            var14 = 0
            while (var14 < 5) {
                if (oscillatorVolume[var14] != 0) {
                    var15 = Instrument_delays[var14] + var11
                    if (var15 < var1) {
                        var10000 = Instrument_samples
                        var10000[var15] += this.evaluateWave(Instrument_phases[var14], var13 * Instrument_volumeSteps[var14] shr 15, pitch!!.form)
                        var10000 = Instrument_phases
                        var10000[var14] += (var12 * Instrument_pitchSteps[var14] shr 16) + Instrument_pitchBaseSteps[var14]
                    }
                }
                ++var14
            }
            ++var11
        }

        var var16: Int
        if (release != null) {
            release!!.reset()
            attack!!.reset()
            var11 = 0
            val var19 = false
            var var20 = true
            var14 = 0
            while (var14 < var1) {
                var15 = release!!.doStep(var1)
                var16 = attack!!.doStep(var1)
                var12 = if (var20) {
                    (var15 * (release!!.end - release!!.start) shr 8) + release!!.start
                } else {
                    (var16 * (release!!.end - release!!.start) shr 8) + release!!.start
                }
                var11 += 256
                if (var11 >= var12) {
                    var11 = 0
                    var20 = !var20
                }
                if (var20) {
                    Instrument_samples[var14] = 0
                }
                ++var14
            }
        }

        if (delayTime > 0 && delayDecay > 0) {
            var11 = (delayTime.toDouble() * var3).toInt()
            var12 = var11
            while (var12 < var1) {
                var10000 = Instrument_samples
                var10000[var12] += Instrument_samples[var12 - var11] * delayDecay / 100
                ++var12
            }
        }

        if (filter!!.pairs[0] > 0 || filter!!.pairs[1] > 0) {
            filterEnvelope!!.reset()
            var11 = filterEnvelope!!.doStep(var1 + 1)
            var12 = filter!!.compute(0, var11.toFloat() / 65536.0f)
            var13 = filter!!.compute(1, var11.toFloat() / 65536.0f)
            if (var1 >= var12 + var13) {
                var14 = 0
                var15 = var13
                if (var13 > var1 - var12) {
                    var15 = var1 - var12
                }
                var var17: Int
                while (var14 < var15) {
                    var16 = (Instrument_samples[var14 + var12].toLong() * SoundEffectAudioFilter.forwardMultiplier.toLong() shr 16).toInt()
                    var17 = 0
                    while (var17 < var12) {
                        var16 += (Instrument_samples[var14 + var12 - 1 - var17].toLong() * SoundEffectAudioFilter.coefficients[0]!![var17].toLong() shr 16).toInt()
                        ++var17
                    }
                    var17 = 0
                    while (var17 < var14) {
                        var16 -= (Instrument_samples[var14 - 1 - var17].toLong() * SoundEffectAudioFilter.coefficients[1]!![var17].toLong() shr 16).toInt()
                        ++var17
                    }
                    Instrument_samples[var14] = var16
                    var11 = filterEnvelope!!.doStep(var1 + 1)
                    ++var14
                }
                val var21 = true
                var15 = 128
                while (true) {
                    if (var15 > var1 - var12) {
                        var15 = var1 - var12
                    }
                    var var18: Int
                    while (var14 < var15) {
                        var17 = (Instrument_samples[var14 + var12].toLong() * SoundEffectAudioFilter.forwardMultiplier.toLong() shr 16).toInt()
                        var18 = 0
                        while (var18 < var12) {
                            var17 += (Instrument_samples[var14 + var12 - 1 - var18].toLong() * SoundEffectAudioFilter.coefficients[0]!![var18].toLong() shr 16).toInt()
                            ++var18
                        }
                        var18 = 0
                        while (var18 < var13) {
                            var17 -= (Instrument_samples[var14 - 1 - var18].toLong() * SoundEffectAudioFilter.coefficients[1]!![var18].toLong() shr 16).toInt()
                            ++var18
                        }
                        Instrument_samples[var14] = var17
                        var11 = filterEnvelope!!.doStep(var1 + 1)
                        ++var14
                    }
                    if (var14 >= var1 - var12) {
                        while (var14 < var1) {
                            var17 = 0
                            var18 = var14 + var12 - var1
                            while (var18 < var12) {
                                var17 += (Instrument_samples[var14 + var12 - 1 - var18].toLong() * SoundEffectAudioFilter.coefficients[0]!![var18].toLong() shr 16).toInt()
                                ++var18
                            }
                            var18 = 0
                            while (var18 < var13) {
                                var17 -= (Instrument_samples[var14 - 1 - var18].toLong() * SoundEffectAudioFilter.coefficients[1]!![var18].toLong() shr 16).toInt()
                                ++var18
                            }
                            Instrument_samples[var14] = var17
                            filterEnvelope!!.doStep(var1 + 1)
                            ++var14
                        }
                        break
                    }
                    var12 = filter!!.compute(0, var11.toFloat() / 65536.0f)
                    var13 = filter!!.compute(1, var11.toFloat() / 65536.0f)
                    var15 += 128
                }
            }
        }

        var11 = 0
        while (var11 < var1) {
            if (Instrument_samples[var11] < -32768) {
                Instrument_samples[var11] = -32768
            }
            if (Instrument_samples[var11] > 32767) {
                Instrument_samples[var11] = 32767
            }
            ++var11
        }

        return Instrument_samples
    }

    private fun evaluateWave(var1: Int, var2: Int, var3: Int): Int = when (var3) {
        1 -> if (var1 and 32767 < 16384) var2 else -var2
        2 -> Instrument_sine[var1 and 32767] * var2 shr 14
        3 -> (var2 * (var1 and 32767) shr 14) - var2
        else -> if (var3 == 4) var2 * Instrument_noise[var1 / 2607 and 32767] else 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SoundEffectInstrument

        if (buffer != other.buffer) return false
        if (!oscillatorVolume.contentEquals(other.oscillatorVolume)) return false
        if (!oscillatorPitch.contentEquals(other.oscillatorPitch)) return false
        if (!oscillatorDelays.contentEquals(other.oscillatorDelays)) return false
        if (delayTime != other.delayTime) return false
        if (delayDecay != other.delayDecay) return false
        if (duration != other.duration) return false
        if (offset != other.offset) return false
        if (pitch != other.pitch) return false
        if (volume != other.volume) return false
        if (pitchModifier != other.pitchModifier) return false
        if (pitchModifierAmplitude != other.pitchModifierAmplitude) return false
        if (volumeMultiplier != other.volumeMultiplier) return false
        if (volumeMultiplierAmplitude != other.volumeMultiplierAmplitude) return false
        if (release != other.release) return false
        if (attack != other.attack) return false
        if (filter != other.filter) return false
        if (filterEnvelope != other.filterEnvelope) return false

        return true
    }

    override fun hashCode(): Int {
        var result = buffer.hashCode()
        result = 31 * result + oscillatorVolume.contentHashCode()
        result = 31 * result + oscillatorPitch.contentHashCode()
        result = 31 * result + oscillatorDelays.contentHashCode()
        result = 31 * result + delayTime
        result = 31 * result + delayDecay
        result = 31 * result + duration
        result = 31 * result + offset
        result = 31 * result + (pitch?.hashCode() ?: 0)
        result = 31 * result + (volume?.hashCode() ?: 0)
        result = 31 * result + (pitchModifier?.hashCode() ?: 0)
        result = 31 * result + (pitchModifierAmplitude?.hashCode() ?: 0)
        result = 31 * result + (volumeMultiplier?.hashCode() ?: 0)
        result = 31 * result + (volumeMultiplierAmplitude?.hashCode() ?: 0)
        result = 31 * result + (release?.hashCode() ?: 0)
        result = 31 * result + (attack?.hashCode() ?: 0)
        result = 31 * result + (filter?.hashCode() ?: 0)
        result = 31 * result + (filterEnvelope?.hashCode() ?: 0)
        return result
    }

    private companion object {
        val Instrument_noise = IntArray(32768)
        val Instrument_sine = IntArray(32768)
        val Instrument_samples = IntArray(220500)
        val Instrument_phases = IntArray(5)
        val Instrument_delays = IntArray(5)
        val Instrument_volumeSteps = IntArray(5)
        val Instrument_pitchSteps = IntArray(5)
        val Instrument_pitchBaseSteps = IntArray(5)
        init {
            val random = Random(0L)
            repeat(32768) {
                Instrument_noise[it] = (random.nextInt() and 2) - 1
            }
            repeat(32768) {
                Instrument_sine[it] = (sin(it.toDouble() / 5215.1903) * 16384.0).toInt()
            }
        }

        fun clearIntArray(array: IntArray, from: Int, to: Int) {
            var var1 = from
            var var2 = to
            var2 = var2 + var1 - 7
            while (var1 < var2) {
                array[var1++] = 0
                array[var1++] = 0
                array[var1++] = 0
                array[var1++] = 0
                array[var1++] = 0
                array[var1++] = 0
                array[var1++] = 0
                array[var1++] = 0
            }
            var2 += 7
            while (var1 < var2) {
                array[var1++] = 0
            }
        }
    }
}
