package xlitekt.application

import com.github.michaelbull.logging.InlineLogger
import io.github.classgraph.ClassGraph
import io.ktor.application.Application
import io.ktor.application.log
import io.ktor.server.engine.commandLineEnvironment
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.ktor.ext.get
import org.koin.ktor.ext.modules
import xlitekt.cache.cacheModule
import xlitekt.game.Game
import xlitekt.game.gameModule
import xlitekt.network.Network
import xlitekt.network.networkModule
import java.util.TimeZone
import kotlin.script.templates.standard.ScriptTemplateWithArgs
import kotlin.system.measureTimeMillis

/**
 * @author Jordan Abraham
 */
private val logger = InlineLogger()

fun main(args: Array<String>) = commandLineEnvironment(args).start()

fun Application.module() {
    val time = measureTimeMillis {
        addShutdownHook()
        logger.info { "Starting XliteKt." }
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        installKoin()
        installKotlinScript()
        get<Game>().start()
    }
    logger.info {
        "\n" +
            "                                                                                                           \n" +
            "                 .---.                                                  .-''-.                             \n" +
            "                 |   |.--.               __.....__                    .' .-.  )                            \n" +
            "                 |   ||__|           .-''         '.                 / .'  / /                             \n" +
            "                 |   |.--.     .|   /     .-''\"'-.  `.              (_/   / /               .-''` ''-.     \n" +
            "   ____     _____|   ||  |   .' |_ /     /________\\   \\                  / /              .'          '.   \n" +
            "  `.   \\  .'    /|   ||  | .'     ||                  |                 / /              /              `  \n" +
            "    `.  `'    .' |   ||  |'--.  .-'\\    .-------------'                . '              '                ' \n" +
            "      '.    .'   |   ||  |   |  |   \\    '-.____...---.               / /    _.-'),.--. |         .-.    | \n" +
            "      .'     `.  |   ||__|   |  |    `.             .'              .' '  _.'.-''//    \\.        |   |   . \n" +
            "    .'  .'`.   `.'---'       |  '.'    `''-...... -'               /  /.-'_.'    \\\\    / .       '._.'  /  \n" +
            "  .'   /    `.   `.          |   /                                /    _.'        `'--'   '._         .'   \n" +
            " '----'       '----'         `'-'                                ( _.-'                      '-....-'`     \n"
    }
    logger.debug { "XliteKt launched in $time ms." }
    get<Network>().awaitOnPort(environment.config.property("ktor.deployment.port").getString().toInt())
}

fun Application.installKoin() {
    modules(
        module { single { this@installKoin.environment } },
        cacheModule,
        networkModule,
        gameModule,
    )
    log.debug("Installed koin modules.")
}

fun Application.installKotlinScript() {
    ClassGraph().enableClassInfo().scan().use { result ->
        result.allClasses.filter { it.extendsSuperclass(ScriptTemplateWithArgs::class.java) }
            .map { it.loadClass().constructors.first().newInstance(emptyArray<String>()) }
            .count()
    }.let { log.debug("Installed $it kotlin scripts.") }
}

fun Application.addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(
        Thread {
            logger.debug { "Running shutdown hook..." }
            get<Game>().shutdown()
            get<Network>().shutdown()
            logger.debug { "Stopping koin..." }
            stopKoin()
        }
    )
}
