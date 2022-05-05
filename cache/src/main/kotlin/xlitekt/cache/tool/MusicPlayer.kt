package xlitekt.cache.tool

import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import xlitekt.cache.cacheModule
import xlitekt.cache.provider.music.MusicEntryTypeProvider
import xlitekt.shared.inject
import java.io.ByteArrayInputStream
import java.nio.file.Path
import java.util.Scanner
import javax.sound.midi.MidiSystem
import kotlin.system.exitProcess

/**
 * @author Jordan Abraham
 */
fun main() {
    startKoin {
        loadKoinModules(cacheModule)
    }

    val musics by inject<MusicEntryTypeProvider>()
    var trackId = -1
    val sequencer = MidiSystem.getSequencer()

    val scanner = Scanner(System.`in`)
    while (true) {
        if (trackId == -1) {
            println("Enter track id...")
            trackId = scanner.nextInt()
            val entry = musics.entryType(trackId) ?: run {
                println("Not found!")
                exitProcess(0)
            }
            val synthesizer = MidiSystem.getSynthesizer()
            synthesizer.open()
            synthesizer.unloadAllInstruments(synthesizer.defaultSoundbank)
            synthesizer.loadAllInstruments(MidiSystem.getSoundbank(Path.of("./cache/data/dump/soundbank/soundbank.sf2").toFile()))

            sequencer.open()
            sequencer.transmitter.receiver = synthesizer.receiver
            sequencer.sequence = MidiSystem.getSequence(ByteArrayInputStream(entry.bytes!!))

            Thread.sleep(1000L)
            sequencer.start()
        } else {
            println("Enter next track id...")
            trackId = scanner.nextInt()
            val entry = musics.entryType(trackId) ?: run {
                println("Not found!")
                exitProcess(0)
            }
            sequencer.stop()
            sequencer.sequence = MidiSystem.getSequence(ByteArrayInputStream(entry.bytes!!))
            Thread.sleep(1000L)
            sequencer.start()
        }
    }
}
