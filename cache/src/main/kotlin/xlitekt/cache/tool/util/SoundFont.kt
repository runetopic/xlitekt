package xlitekt.cache.tool.util

import com.sun.media.SF2Instrument
import com.sun.media.SF2InstrumentRegion
import com.sun.media.SF2Layer
import com.sun.media.SF2LayerRegion
import com.sun.media.SF2Region
import com.sun.media.SF2Sample
import com.sun.media.SF2Soundbank
import xlitekt.cache.provider.instrument.InstrumentEntryType
import xlitekt.cache.provider.soundeffect.SoundEffectEntryTypeProvider
import xlitekt.cache.provider.vorbis.VorbisEntryTypeProvider
import xlitekt.shared.inject
import javax.sound.midi.Patch
import xlitekt.cache.provider.instrument.InstrumentSample

/**
 * @author Jordan Abraham
 */
internal class SoundFont {
    val sf2Soundbank = SF2Soundbank()
    private var sf2Instrument: SF2Instrument? = null
    private var sf2Sample: SF2Sample? = null
    private var sf2Layer: SF2Layer? = null
    private var previousId: Int = -1

    fun addSamples(entry: InstrumentEntryType, addedInstruments: MutableList<Int>) {
        val keyRange = ByteArray(2)
        sf2Instrument = null
        sf2Layer = null
        sf2Sample = null
        var audioBuffer: InstrumentSample? = null

        var var5 = 0
        repeat(128) { key ->
            var offset = entry.offsets!![key]
            if (offset != 0 && offset != var5) {
                if (offset != var5) {
                    var5 = offset--
                    val instrumentId = offset shr 2
                    if (instrumentId in addedInstruments) return@repeat
                    audioBuffer = if (offset and 1 == 0) {
                        val soundEffects by inject<SoundEffectEntryTypeProvider>()
                        soundEffects.entryType(instrumentId)?.toInstrumentSample(null)!!
                    } else {
                        val vorbis by inject<VorbisEntryTypeProvider>()
                        vorbis.entryType(instrumentId)?.toInstrumentSample(null)!!
                    }
                }

                if (audioBuffer != null) {
                    addedInstruments += offset shr 2

                    var nextKey = key
                    for (index in key + 1..127) {
                        val nextOffset = entry.offsets!![index]
                        if (nextOffset != 0 && nextOffset != var5) {
                            nextKey = index
                            break
                        }
                    }

                    val samples = audioBuffer!!.samples
                    val audio = ByteArray(samples.size * 2)

                    repeat(samples.size) {
                        audio[it * 2] = samples[it]
                        audio[it * 2 + 1] = samples[it]
                    }

                    var originalPitch = (entry.pitchOffset!![key] / 256) + 128
                    while (originalPitch > 127) {
                        originalPitch -= 128
                    }

                    sf2Sample = SF2Sample()
                    sf2Sample!!.name = "${offset shr 2}"
                    sf2Sample!!.setData(audio)
                    sf2Sample!!.originalPitch = originalPitch
                    sf2Sample!!.sampleRate = audioBuffer!!.sampleRate.toLong()
                    sf2Sample!!.startLoop = audioBuffer!!.start.toLong()
                    sf2Sample!!.endLoop = audioBuffer!!.end.toLong()
                    sf2Sample!!.sampleType = 1
                    sf2Sample!!.sampleLink = 0

                    if (entry.id != previousId) {
                        sf2Layer = null
                        sf2Instrument = null
                    }

                    keyRange[0] = key.toByte()
                    keyRange[1] = if (nextKey == key) 127 else nextKey.toByte()

                    sf2Soundbank.addResource(sf2Sample)
                    if (sf2Instrument == null) {
                        sf2Instrument = SF2Instrument()
                        sf2Instrument!!.name = "Patch ${entry.id}"
                        sf2Instrument!!.patch = Patch(entry.id.let { i -> if (i > 127) 127 + (entry.id / 128) else 0 }, entry.id.let { i -> if (i > 127) i - 128 else i })
                    }

                    if (sf2Layer == null) {
                        sf2Layer = SF2Layer()
                        sf2Layer!!.name = "Patch ${entry.id}"
                    }

                    val layerRegion = SF2LayerRegion()
                    layerRegion.sample = sf2Sample
                    layerRegion.putBytes(SF2Region.GENERATOR_KEYRANGE, keyRange)
                    layerRegion.putShort(SF2Region.GENERATOR_FINETUNE, 0)
                    layerRegion.putInteger(SF2Region.GENERATOR_SAMPLEMODES, entry.loopMode!![keyRange[0].toInt()] * -1)
                    layerRegion.putInteger(SF2Region.GENERATOR_PAN, ((entry.panOffset!![keyRange[0].toInt()] - 64).toShort()).toInt())
                    layerRegion.putInteger(SF2Region.GENERATOR_INITIALATTENUATION, ((entry.volumeOffset!![keyRange[0].toInt()] + entry.baseVelocity).toShort()).toInt())

                    sf2Layer!!.regions.add(layerRegion)

                    val instrumentRegion = SF2InstrumentRegion()
                    instrumentRegion.layer = sf2Layer

                    if (sf2Soundbank.resources.none { it.name == "Patch ${entry.id}" }) {
                        sf2Soundbank.addResource(sf2Layer)
                    }
                    if (sf2Instrument!!.regions.none { it.layer.name == sf2Layer!!.name }) {
                        sf2Instrument!!.regions.add(instrumentRegion)
                    }

                    if (sf2Soundbank.getInstrument(sf2Instrument!!.patch) == null) {
                        sf2Soundbank.addInstrument(sf2Instrument)
                    }

                    previousId = entry.id
                    entry.offsets!![key] = 0
                }
            }
        }
    }
}
