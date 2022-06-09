package xlitekt.cache.provider.soundeffect

import xlitekt.shared.buffer.discard
import xlitekt.shared.buffer.readShortSmart
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.buffer.readUShortSmart
import xlitekt.shared.buffer.tryPeek
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
        when {
            buffer.tryPeek() != 0 -> {
                pitchModifier = SoundEffectEnvelope(buffer)
                pitchModifierAmplitude = SoundEffectEnvelope(buffer)
            }
            else -> buffer.discard(1)
        }
        when {
            buffer.tryPeek() != 0 -> {
                volumeMultiplier = SoundEffectEnvelope(buffer)
                volumeMultiplierAmplitude = SoundEffectEnvelope(buffer)
            }
            else -> buffer.discard(1)
        }
        when {
            buffer.tryPeek() != 0 -> {
                release = SoundEffectEnvelope(buffer)
                attack = SoundEffectEnvelope(buffer)
            }
            else -> buffer.discard(1)
        }
        for (index in 0 until 10) {
            val var4 = buffer.readUShortSmart()
            if (var4 == 0) break
            oscillatorVolume[index] = var4
            oscillatorPitch[index] = buffer.readShortSmart()
            oscillatorDelays[index] = buffer.readUShortSmart()
        }
        delayTime = buffer.readUShortSmart()
        delayDecay = buffer.readUShortSmart()
        duration = buffer.readUShort()
        offset = buffer.readUShort()
        filter = SoundEffectAudioFilter()
        filterEnvelope = SoundEffectEnvelope()
        filter!!.method1089(buffer, filterEnvelope!!)
    }

    fun synthesize(var1: Int, var2: Int): IntArray {
        clearIntArray(Instrument_samples, 0, var1)
        if (var2 < 10) return Instrument_samples
        val var3 = var1.toDouble() / (var2.toDouble() + 0.0)
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

        repeat(5) {
            if (oscillatorVolume[it] != 0) {
                Instrument_phases[it] = 0
                Instrument_delays[it] = (oscillatorDelays[it].toDouble() * var3).toInt()
                Instrument_volumeSteps[it] = (oscillatorVolume[it] shl 14) / 100
                Instrument_pitchSteps[it] = ((pitch!!.end - pitch!!.start).toDouble() * 32.768 * 1.0057929410678534.pow(oscillatorPitch[it].toDouble()) / var3).toInt()
                Instrument_pitchBaseSteps[it] = (pitch!!.start.toDouble() * 32.768 / var3).toInt()
            }
        }

        var var14: Int
        var var15: Int
        var var10000: IntArray
        repeat(var1) {
            var var12 = pitch!!.doStep(var1)
            var var13 = volume!!.doStep(var1)
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
            repeat(5) { var14 ->
                if (oscillatorVolume[var14] != 0) {
                    var15 = Instrument_delays[var14] + it
                    if (var15 < var1) {
                        var10000 = Instrument_samples
                        var10000[var15] += this.evaluateWave(Instrument_phases[var14], var13 * Instrument_volumeSteps[var14] shr 15, pitch!!.form)
                        var10000 = Instrument_phases
                        var10000[var14] += (var12 * Instrument_pitchSteps[var14] shr 16) + Instrument_pitchBaseSteps[var14]
                    }
                }
            }
        }

        if (release != null) {
            release!!.reset()
            attack!!.reset()
            var var11 = 0
            var var20 = true
            repeat(var1) {
                val releaseStep = release!!.doStep(var1)
                val attackStep = attack!!.doStep(var1)
                val var12 = if (var20) {
                    (releaseStep * (release!!.end - release!!.start) shr 8) + release!!.start
                } else {
                    (attackStep * (release!!.end - release!!.start) shr 8) + release!!.start
                }
                var11 += 256
                if (var11 >= var12) {
                    var11 = 0
                    var20 = !var20
                }
                if (var20) {
                    Instrument_samples[it] = 0
                }
            }
        }

        if (delayTime > 0 && delayDecay > 0) {
            val var11 = (delayTime.toDouble() * var3).toInt()
            for (index in var11 until var1) {
                var10000 = Instrument_samples
                var10000[index] += Instrument_samples[index - var11] * delayDecay / 100
            }
        }

        if (filter!!.pairs[0] > 0 || filter!!.pairs[1] > 0) {
            filterEnvelope!!.reset()
            var var11 = filterEnvelope!!.doStep(var1 + 1)
            var var12 = filter!!.compute(0, var11.toFloat() / 65536.0f)
            var var13 = filter!!.compute(1, var11.toFloat() / 65536.0f)
            if (var1 >= var12 + var13) {
                var14 = 0
                var var151 = var13
                if (var13 > var1 - var12) {
                    var151 = var1 - var12
                }
                while (var14 < var151) {
                    var var16 = (Instrument_samples[var14 + var12].toLong() * SoundEffectAudioFilter.forwardMultiplier.toLong() shr 16).toInt()
                    repeat(var12) { var17 ->
                        var16 += (Instrument_samples[var14 + var12 - 1 - var17].toLong() * SoundEffectAudioFilter.coefficients[0]!![var17].toLong() shr 16).toInt()
                    }
                    repeat(var14) { var17 ->
                        var16 -= (Instrument_samples[var14 - 1 - var17].toLong() * SoundEffectAudioFilter.coefficients[1]!![var17].toLong() shr 16).toInt()
                    }
                    Instrument_samples[var14] = var16
                    var11 = filterEnvelope!!.doStep(var1 + 1)
                    ++var14
                }
                var151 = 128
                while (true) {
                    if (var151 > var1 - var12) {
                        var151 = var1 - var12
                    }
                    while (var14 < var151) {
                        var var17 = (Instrument_samples[var14 + var12].toLong() * SoundEffectAudioFilter.forwardMultiplier.toLong() shr 16).toInt()
                        repeat(var12) {
                            var17 += (Instrument_samples[var14 + var12 - 1 - it].toLong() * SoundEffectAudioFilter.coefficients[0]!![it].toLong() shr 16).toInt()
                        }
                        repeat(var13) {
                            var17 -= (Instrument_samples[var14 - 1 - it].toLong() * SoundEffectAudioFilter.coefficients[1]!![it].toLong() shr 16).toInt()
                        }
                        Instrument_samples[var14] = var17
                        var11 = filterEnvelope!!.doStep(var1 + 1)
                        ++var14
                    }
                    if (var14 >= var1 - var12) {
                        while (var14 < var1) {
                            var var17 = 0
                            for (index in (var14 + var12 - var1) until var12) {
                                var17 += (Instrument_samples[var14 + var12 - 1 - index].toLong() * SoundEffectAudioFilter.coefficients[0]!![index].toLong() shr 16).toInt()
                            }
                            for (index in 0 until var13) {
                                var17 -= (Instrument_samples[var14 - 1 - index].toLong() * SoundEffectAudioFilter.coefficients[1]!![index].toLong() shr 16).toInt()
                            }
                            Instrument_samples[var14] = var17
                            filterEnvelope!!.doStep(var1 + 1)
                            ++var14
                        }
                        break
                    }
                    var12 = filter!!.compute(0, var11.toFloat() / 65536.0f)
                    var13 = filter!!.compute(1, var11.toFloat() / 65536.0f)
                    var151 += 128
                }
            }
        }

        repeat(var1) {
            if (Instrument_samples[it] < -32768) {
                Instrument_samples[it] = -32768
            }
            if (Instrument_samples[it] > 32767) {
                Instrument_samples[it] = 32767
            }
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
