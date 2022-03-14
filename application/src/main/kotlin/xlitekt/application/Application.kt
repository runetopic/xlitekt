package xlitekt.application

import com.github.michaelbull.logging.InlineLogger
import io.github.classgraph.ClassGraph
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.log
import io.ktor.response.respondBytes
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.util.TimeZone
import kotlin.script.templates.standard.ScriptTemplateWithArgs
import kotlin.system.measureTimeMillis
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.ktor.ext.get
import org.koin.ktor.ext.modules
import xlitekt.cache.cacheModule
import xlitekt.game.Game
import xlitekt.game.gameModule
import xlitekt.network.Network
import xlitekt.network.networkModule
import xlitekt.shared.config.JavaConfig
import xlitekt.shared.inject
import xlitekt.shared.sharedModule

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
        installHttpServer()
        get<Game>().start()
    }
    logger.info {
        "\n" +
            "                                                                             \n" +
            "                 .---.                                                       \n" +
            "                 |   |.--.               __.....__          .                \n" +
            "                 |   ||__|           .-''         '.      .'|                \n" +
            "                 |   |.--.     .|   /     .-''\"'-.  `.  .'  |           .|   \n" +
            "   ____     _____|   ||  |   .' |_ /     /________\\   \\<    |         .' |_  \n" +
            "  `.   \\  .'    /|   ||  | .'     ||                  | |   | ____  .'     | \n" +
            "    `.  `'    .' |   ||  |'--.  .-'\\    .-------------' |   | \\ .' '--.  .-' \n" +
            "      '.    .'   |   ||  |   |  |   \\    '-.____...---. |   |/  .     |  |   \n" +
            "      .'     `.  |   ||__|   |  |    `.             .'  |    \\  \\    |  |   \n" +
            "    .'  .'`.   `.'---'       |  '.'    `''-...... -'    |   |  \\  \\   |  '.' \n" +
            "  .'   /    `.   `.          |   /                      '    \\  \\  \\  |   /  \n" +
            " '----'       '----'         `'-'                      '------'  '---'`'-'   \n"
    }
    logger.debug { "XliteKt launched in $time ms." }
    get<Network>().awaitOnPort(environment.config.property("ktor.deployment.port").getString().toInt())
}

fun Application.installHttpServer() {
    val javaConfig by inject<JavaConfig>()

    embeddedServer(Netty, port = 8080) {
        routing {
            get("/jav_config.ws") {
                call.respondBytes { javaConfig.fileAsBytes }
            }
            get("/gamepack.jar") {
                call.respondBytes { javaConfig.gamePackAsBytes }
            }
            get("/browsercontrol_0.jar") {
                call.respondBytes { javaConfig.browserControl0 }
            }
            get("/browsercontrol_1.jar") {
                call.respondBytes { javaConfig.browserControl1 }
            }
        }
    }.start(wait = false)
}

fun Application.installKoin() {
    modules(
        module { single { this@installKoin.environment } },
        cacheModule,
        gameModule,
        networkModule,
        sharedModule
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
