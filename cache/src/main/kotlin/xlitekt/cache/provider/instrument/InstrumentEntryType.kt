package xlitekt.cache.provider.instrument

import com.sun.media.SF2Instrument
import com.sun.media.SF2InstrumentRegion
import com.sun.media.SF2Layer
import com.sun.media.SF2LayerRegion
import com.sun.media.SF2Region
import com.sun.media.SF2Sample
import com.sun.media.SF2Soundbank
import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.soundeffect.SoundEffectEntryTypeProvider
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider
import xlitekt.shared.inject
import javax.sound.midi.Patch

/**
 * @author Jordan Abraham
 */
data class InstrumentEntryType(
    override val id: Int,
    var instrumentSamples: Array<InstrumentSample?>? = null,
    var pitchOffset: ShortArray? = null,
    var volumeOffset: ByteArray? = null,
    var panOffset: ByteArray? = null,
    var field3117: Array<Instrument?>? = null,
    var loopMode: ByteArray? = null,
    var groupIdOffsets: IntArray? = null,
    var baseVelocity: Int = 0,
    var bytes: ByteArray? = null
) : EntryType(id) {
    fun loadVorbisSamples(bytes: ByteArray?, samples: IntArray): Boolean {
        var loaded = true
        val noteRange = ByteArray(2)
        var nextNoteRange = 0
        var offset = 0
        repeat(128) {
            if (bytes == null || bytes[it].toInt() != 0) {
                var idOffset = groupIdOffsets!![it]
                if (idOffset != 0) {
                    var instrumentSample: InstrumentSample? = null
                    if (offset != idOffset) {
                        offset = idOffset--
                        instrumentSample = if (idOffset and 1 == 0) {
                            getSoundEffect(idOffset shr 2, samples)
                        } else {
                            getInstrumentForTrack(idOffset shr 2, samples)
                        }
                        if (instrumentSample == null) {
                            loaded = false
                        }
                    }

                    if (instrumentSample != null) {
                        val data = instrumentSample.samples
                        val audio = ByteArray(data.size * 2)

                        repeat(data.size) { i ->
                            audio[i * 2] + data[i]
                            audio[i * 2 + 1] = data[i]
                        }

                        var samplePitch = pitchOffset!![it] / 256 + 128
                        while (samplePitch > 127) {
                            samplePitch -= 128
                        }

                        val sample = SF2Sample()
                        sample.name = (idOffset shr 2).toString()
                        sample.setData(audio)
                        sample.originalPitch = samplePitch
                        sample.sampleRate = instrumentSample.sampleRate.toLong()
                        sample.startLoop = instrumentSample.start.toLong()
                        sample.endLoop = instrumentSample.end.toLong()
                        sample.sampleType = 1
                        sample.sampleLink = 0

                        repeat(128) { i ->
                            if (groupIdOffsets!![i] == offset) {
                                noteRange[0] = i.toByte()
                                noteRange[1] = i.toByte()
                                if (nextNoteRange < 128) {
                                    addSample(sample, noteRange, nextNoteRange)
                                }
                                nextNoteRange++
                            }
                        }
                        noteRange[0] = it.toByte()
                        noteRange[1] = nextNoteRange.toByte()
                        if (nextNoteRange >= 127) {
                            sf2Instrument = null
                        }

                        instrumentSamples!![it] = instrumentSample
                        groupIdOffsets!![it] = 0
                    }
                }
            }
        }
        return loaded
    }

    private fun getInstrumentForTrack(id: Int, samples: IntArray): InstrumentSample? {
        val packedId = id xor (0 shl 4 and 65535 or 0 ushr 12)
        val entry = vorbis.entryType(id) ?: return null
        val sample = entry.toInstrumentSample(samples) ?: return null
        globalInstrumentSamples[packedId] = sample
        return sample
    }

    private fun getSoundEffect(id: Int, samples: IntArray): InstrumentSample? {
        val packedId = 0 xor (id shl 4 and 65535 or id ushr 12)
        val entry = soundEffects.entryType(id) ?: return null
        val sample = entry.toInstrumentSample(samples)
        globalInstrumentSamples[packedId] = sample
        return sample
    }

    private fun addSample(sample: SF2Sample, noteRange: ByteArray, nextNoteRange: Int) {
        globalSoundBank.addResource(sample)

        if (sf2Instrument == null) {
            sf2Instrument = SF2Instrument()
            sf2Instrument!!.name = "Patch $id"
            sf2Instrument!!.patch = Patch(0, id)
            if (id <= 128) {
                var bank = 0
                var program = id
                while (program < 127) {
                    program -= 128
                    bank++
                }
                sf2Instrument!!.patch = Patch(bank, id)
            }
        }
        val layer = SF2Layer()
        layer.name = "Patch $id"
        val region = SF2LayerRegion()
        region.sample = sample
        region.putBytes(SF2Region.GENERATOR_KEYRANGE, noteRange)
        var pitchOffset = (pitchOffset!![nextNoteRange] / 256 + 128).toShort()
        var pitchCorrection = this.pitchOffset!![nextNoteRange]

        while (pitchOffset >= 128) {
            pitchOffset = (pitchOffset - 128).toShort()
        }

        while (pitchCorrection <= -256) {
            pitchCorrection = (pitchCorrection + 256).toShort()
        }

        while (pitchCorrection >= 256) {
            pitchCorrection = (pitchCorrection - 256).toShort()
        }

        region.putShort(SF2Region.GENERATOR_OVERRIDINGROOTKEY, pitchOffset)
        region.putShort(SF2Region.GENERATOR_FINETUNE, pitchCorrection)
        region.putInteger(SF2Region.GENERATOR_SAMPLEMODES, loopMode!![nextNoteRange] * -1)
        region.putInteger(SF2Region.GENERATOR_PAN, panOffset!![nextNoteRange] - 64)
        region.putInteger(SF2Region.GENERATOR_INITIALATTENUATION, volumeOffset!![nextNoteRange] + baseVelocity)
        region.putShort(SF2Region.GENERATOR_RELEASEVOLENV, 0)
        region.putShort(SF2Region.GENERATOR_INITIALATTENUATION, attenuation(baseVelocity).toInt().toShort())
        region.putBytes(SF2Region.GENERATOR_ATTACKVOLENV, byteArrayOf(0, 64))
        layer.regions.add(region)
        val instrumentRegion = SF2InstrumentRegion()
        instrumentRegion.layer = layer
        if (globalSoundBanks[id] == null) {
            globalSoundBanks[id] = SF2Soundbank().also {
                it.name = "soundbank"
                it.romName = "osrs"
                it.romVersionMajor = 2
                it.romVersionMinor = 0
            }
        }
        globalSoundBanks[id]!!.addResource(sample)
        globalSoundBanks[id]!!.addResource(layer)
        globalSoundBank.addResource(layer)
        sf2Instrument!!.regions.add(instrumentRegion)
        if (globalSoundBanks[id]!!.getInstrument(sf2Instrument!!.patch) == null) {
            globalSoundBanks[id]!!.addInstrument(sf2Instrument!!)
        }
        if (globalSoundBank.getInstrument(sf2Instrument!!.patch) == null) {
            globalSoundBank.addInstrument(sf2Instrument)
        }
    }

    private fun attenuation(value: Int): Double = when (value) {
        0 -> 80.00 * 25
        1 -> 78.2 * 25
        2 -> 66.2 * 25
        3 -> 59.1 * 25
        4 -> 54.1 * 25
        5 -> 50.3 * 25
        6 -> 47.1 * 25
        7 -> 44.4 * 25
        8 -> 42.1 * 25
        9 -> 40.0 * 25
        10 -> 38.2 * 25
        11 -> 36.6 * 25
        12 -> 35.1 * 25
        13 -> 33.7 * 25
        14 -> 32.4 * 25
        15 -> 31.2 * 25
        16 -> 30.1 * 25
        17 -> 29.0 * 25
        18 -> 28.0 * 25
        19 -> 27.1 * 25
        20 -> 26.2 * 25
        21 -> 25.3 * 25
        22 -> 24.5 * 25
        23 -> 23.8 * 25
        24 -> 23.0 * 25
        25 -> 22.3 * 25
        26 -> 21.6 * 25
        27 -> 21.0 * 25
        28 -> 20.3 * 25
        29 -> 19.7 * 25
        30 -> 19.1 * 25
        31 -> 18.6 * 25
        32 -> 18.0 * 25
        33 -> 17.5 * 25
        34 -> 17.0 * 25
        35 -> 16.5 * 25
        36 -> 16.0 * 25
        37 -> 15.5 * 25
        38 -> 15.0 * 25
        39 -> 14.6 * 25
        40 -> 14.1 * 25
        41 -> 13.7 * 25
        42 -> 13.3 * 25
        43 -> 12.9 * 25
        44 -> 12.5 * 25
        45 -> 12.1 * 25
        46 -> 11.7 * 25
        47 -> 11.3 * 25
        48 -> 11.0 * 25
        49 -> 10.6 * 25
        50 -> 10.3 * 25
        51 -> 9.9 * 25
        52 -> 9.6 * 25
        53 -> 9.2 * 25
        54 -> 8.9 * 25
        55 -> 8.6 * 25
        56 -> 8.3 * 25
        57 -> 8.0 * 25
        58 -> 7.7 * 25
        59 -> 7.4 * 25
        60 -> 7.1 * 25
        61 -> 6.8 * 25
        62 -> 6.5 * 25
        63 -> 6.2 * 25
        64 -> 6.0 * 25
        65 -> 5.7 * 25
        66 -> 5.4 * 25
        67 -> 5.2 * 25
        68 -> 4.9 * 25
        69 -> 4.7 * 25
        70 -> 4.4 * 25
        71 -> 4.2 * 25
        72 -> 3.9 * 25
        73 -> 3.7 * 25
        74 -> 3.5 * 25
        75 -> 3.2 * 25
        76 -> 3.0 * 25
        77 -> 2.8 * 25
        78 -> 2.5 * 25
        79 -> 2.3 * 25
        80 -> 2.1 * 25
        81 -> 1.9 * 25
        82 -> 1.7 * 25
        83 -> 1.5 * 25
        84 -> 1.2 * 25
        85 -> 1.0 * 25
        86 -> 0.8 * 25
        87 -> 0.6 * 25
        88 -> 0.4 * 25
        89 -> 0.2 * 25
        90 -> 0.0 * 25
        91 -> -0.1 * 25
        92 -> -0.3 * 25
        93 -> -0.5 * 25
        94 -> -0.7 * 25
        95 -> -0.9 * 25
        96 -> -1.1 * 25
        97 -> -1.3 * 25
        98 -> -1.4 * 25
        99 -> -1.6 * 25
        100 -> -1.8 * 25
        101 -> -2.0 * 25
        102 -> -2.1 * 25
        103 -> -2.3 * 25
        104 -> -2.5 * 25
        105 -> -2.6 * 25
        106 -> -2.8 * 25
        107 -> -3.0 * 25
        108 -> -3.1 * 25
        109 -> -3.3 * 25
        110 -> -3.4 * 25
        111 -> -3.6 * 25
        112 -> -3.7 * 25
        113 -> -3.9 * 25
        114 -> -4.1 * 25
        115 -> -4.2 * 25
        116 -> -4.4 * 25
        117 -> -4.5 * 25
        118 -> -4.7 * 25
        119 -> -4.8 * 25
        120 -> -4.9 * 25
        121 -> -5.1 * 25
        122 -> -5.2 * 25
        123 -> -5.4 * 25
        124 -> -5.5 * 25
        125 -> -5.7 * 25
        126 -> -5.8 * 25
        127 -> -6.0 * 25
        else -> 0.00
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as InstrumentEntryType

        if (id != other.id) return false
        if (instrumentSamples != null) {
            if (other.instrumentSamples == null) return false
            if (!instrumentSamples.contentEquals(other.instrumentSamples)) return false
        } else if (other.instrumentSamples != null) return false
        if (pitchOffset != null) {
            if (other.pitchOffset == null) return false
            if (!pitchOffset.contentEquals(other.pitchOffset)) return false
        } else if (other.pitchOffset != null) return false
        if (volumeOffset != null) {
            if (other.volumeOffset == null) return false
            if (!volumeOffset.contentEquals(other.volumeOffset)) return false
        } else if (other.volumeOffset != null) return false
        if (panOffset != null) {
            if (other.panOffset == null) return false
            if (!panOffset.contentEquals(other.panOffset)) return false
        } else if (other.panOffset != null) return false
        if (field3117 != null) {
            if (other.field3117 == null) return false
            if (!field3117.contentEquals(other.field3117)) return false
        } else if (other.field3117 != null) return false
        if (loopMode != null) {
            if (other.loopMode == null) return false
            if (!loopMode.contentEquals(other.loopMode)) return false
        } else if (other.loopMode != null) return false
        if (groupIdOffsets != null) {
            if (other.groupIdOffsets == null) return false
            if (!groupIdOffsets.contentEquals(other.groupIdOffsets)) return false
        } else if (other.groupIdOffsets != null) return false
        if (baseVelocity != other.baseVelocity) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + (instrumentSamples?.contentHashCode() ?: 0)
        result = 31 * result + (pitchOffset?.contentHashCode() ?: 0)
        result = 31 * result + (volumeOffset?.contentHashCode() ?: 0)
        result = 31 * result + (panOffset?.contentHashCode() ?: 0)
        result = 31 * result + (field3117?.contentHashCode() ?: 0)
        result = 31 * result + (loopMode?.contentHashCode() ?: 0)
        result = 31 * result + (groupIdOffsets?.contentHashCode() ?: 0)
        result = 31 * result + baseVelocity
        return result
    }

    companion object {
        private val vorbis by inject<VorbisEntryTypeProvider>()
        private val soundEffects by inject<SoundEffectEntryTypeProvider>()
        val globalInstrumentSamples = mutableMapOf<Int, InstrumentSample>()
        val globalSoundBanks = mutableMapOf<Int, SF2Soundbank>()
        val globalSoundBank = SF2Soundbank().also {
            it.name = "soundbank"
            it.romName = "osrs"
            it.romVersionMajor = 2
            it.romVersionMinor = 0
        }
        private var sf2Instrument: SF2Instrument? = null
    }
}
