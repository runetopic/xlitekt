package xlitekt.cache.tool

import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import xlitekt.cache.cacheModule
import xlitekt.cache.provider.music.MusicEntryTypeProvider
import xlitekt.shared.inject
import java.io.ByteArrayInputStream
import java.nio.file.Path
import java.util.Scanner
import java.util.concurrent.TimeUnit
import javax.sound.midi.MidiSystem
import javax.sound.midi.Sequence
import javax.sound.midi.Sequencer
import javax.sound.midi.Synthesizer
import kotlin.io.path.readBytes
import kotlin.system.exitProcess

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
        var sequencer: Sequencer? = null
        var synthesizer: Synthesizer? = null

        val scanner = Scanner(System.`in`)
        while (true) {
            println("Input One Of The Following Options Into The Console...")
            println("----- (0-${musics.size()}) ----- pause ----- play ----- stop ----- list -----")
            val input = scanner.next()
            if (input.toIntOrNull() == null) {
                when (input) {
                    "pause" -> {
                        println("Pausing...")
                        sequencer?.stop()
                    }
                    "play" -> {
                        println("Playing...")
                        sequencer?.start()
                    }
                    "stop" -> {
                        println("Stopping...")
                        sequencer?.stop()
                        synthesizer?.close()
                        Thread.sleep(1000)
                        exitProcess(0)
                    }
                    "list" -> {
                        musics.entries().associate { it.id to it.name }.forEach(::println)
                    }
                }
            } else {
                val entry = musics.entryType(input.toInt()) ?: run {
                    println("Not found!")
                    return
                }

                println("Loading ${entry.name ?: entry.id}...")

                sequencer?.stop()
                synthesizer?.close()

                sequence = MidiSystem.getSequence(ByteArrayInputStream(entry.bytes!!))

                synthesizer = MidiSystem.getSynthesizer()
                synthesizer.open()
                synthesizer.unloadAllInstruments(synthesizer.defaultSoundbank)
                synthesizer.loadAllInstruments(soundBank)

                sequencer = MidiSystem.getSequencer(false)
                sequencer.open()
                sequencer.transmitter.receiver = synthesizer.receiver
                sequencer.sequence = sequence
                sequencer.open()

                Thread.sleep(1000)

                sequencer.start()

                val minutes = TimeUnit.MICROSECONDS.toMinutes(sequence!!.microsecondLength)
                val seconds = TimeUnit.MICROSECONDS.toSeconds(sequence.microsecondLength) % 60
                println("Playing ${entry.name ?: entry.id} ----- $minutes minutes and $seconds seconds long.")
            }
        }
    }
}
