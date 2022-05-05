package xlitekt.cache.tool

import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import xlitekt.cache.cacheModule
import xlitekt.cache.provider.music.MusicEntryTypeProvider
import xlitekt.shared.inject
import java.io.ByteArrayInputStream
import java.nio.file.Path
import java.sql.Time
import java.util.Scanner
import java.util.concurrent.TimeUnit
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequence
import javax.sound.midi.Sequencer
import javax.sound.midi.Synthesizer
import kotlin.io.path.readBytes

/**
 * @author Jordan Abraham
 */
fun main() {
    startKoin {
        loadKoinModules(cacheModule)
    }
    MusicPlayer.start()
}

internal object MusicPlayer {

    fun start() {
        val soundBank = MidiSystem.getSoundbank(ByteArrayInputStream(Path.of("./cache/data/dump/soundbank/soundbank.sf2").readBytes()))

        val musics by inject<MusicEntryTypeProvider>()
        var sequence: Sequence?
        var sequencers: Array<Sequencer?>? = null
        var synthesizers: Array<Synthesizer?>? = null

        val scanner = Scanner(System.`in`)
        while (true) {
            println("Enter track ID (0-${musics.size()})")
            val entry = musics.entryType(scanner.nextInt()) ?: run {
                println("Not found!")
                return
            }

            println("Loading...")

            sequencers?.onEach { it?.stop() }
            synthesizers?.onEach { it?.close() }

            sequence = MidiSystem.getSequence(ByteArrayInputStream(entry.bytes!!))
            val count = sequence.tracks.size
            sequencers = arrayOfNulls(count)
            synthesizers = arrayOfNulls(count)

            repeat(count) {
                synthesizers[it] = MidiSystem.getSynthesizer()
                synthesizers[it]!!.unloadAllInstruments(synthesizers[it]!!.defaultSoundbank)
                synthesizers[it]!!.loadAllInstruments(soundBank)
                synthesizers[it]!!.open()
            }

            repeat(count) {
                sequencers[it] = MidiSystem.getSequencer(false)
                sequencers[it]!!.transmitter.receiver = synthesizers[it]!!.receiver
                sequencers[it]!!.sequence = sequence
                sequencers[it]!!.setTrackSolo(it, true)
                sequencers[it]!!.open()
            }

            Thread.sleep(1000)

            repeat(sequencers.size) {
                sequencers[it]!!.start()
            }
            val minutes = TimeUnit.MICROSECONDS.toMinutes(sequence!!.microsecondLength)
            val seconds = TimeUnit.MICROSECONDS.toSeconds(sequence.microsecondLength) % 60
            println("Playing ${entry.name} --- $minutes minutes and $seconds seconds long.")
        }
    }
}
