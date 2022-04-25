package xlitekt.application

import com.github.michaelbull.logging.InlineLogger
import io.github.classgraph.ClassGraph
import io.ktor.events.EventDefinition
import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationStopped
import io.ktor.server.application.call
import io.ktor.server.application.createApplicationPlugin
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import xlitekt.cache.cacheModule
import xlitekt.game.Game
import xlitekt.game.gameModule
import xlitekt.network.Network
import xlitekt.network.networkModule
import xlitekt.shared.config.JavaConfig
import xlitekt.shared.inject
import xlitekt.shared.lazy
import xlitekt.shared.sharedModule
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
        installHttpServer()
        lazy<Game>().start()
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
    lazy<Network>().awaitOnPort(environment.config.property("ktor.deployment.port").getString().toInt())
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

class KoinConfig {
    internal var modules: ArrayList<Module> = ArrayList()
}

val KoinApplicationStarted = EventDefinition<KoinApplication>()
val KoinApplicationStopPreparing = EventDefinition<KoinApplication>()
val KoinApplicationStopped = EventDefinition<KoinApplication>()

val Koin = createApplicationPlugin(name = "Koin", createConfiguration = ::KoinConfig) {
    val declaration: KoinAppDeclaration = {
        modules(pluginConfig.modules)
    }
    val koinApplication = startKoin(appDeclaration = declaration)
    environment?.monitor?.let { monitor ->
        monitor.raise(KoinApplicationStarted, koinApplication)
        monitor.subscribe(ApplicationStopped) {
            monitor.raise(KoinApplicationStopPreparing, koinApplication)
            stopKoin()
            monitor.raise(KoinApplicationStopped, koinApplication)
        }
    }
}

fun Application.installKoin() {
    // Fix provided by https://github.com/InsertKoinIO/koin/issues/1295
    install(Koin) {
        modules = arrayListOf(
            module { single { this@installKoin.environment } },
            sharedModule,
            cacheModule,
            gameModule,
            networkModule,
        )
    }
    log.debug("Installed koin modules.")
}

fun Application.installKotlinScript() {
    ClassGraph().enableClassInfo().scan().use { result ->
        result.allClasses.filter { it.extendsSuperclass(ScriptTemplateWithArgs::class.java) }
            .map { it.loadClass().constructors.first().newInstance(emptyArray<String>()) }
            .count()
    }.let { log.debug("Installed $it kotlin scripts.") }
}

fun addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(
        Thread {
            logger.debug { "Running shutdown hook..." }
            lazy<Game>().shutdown()
            lazy<Network>().shutdown()
            logger.debug { "Stopping koin..." }
            stopKoin()
        }
    )
}
