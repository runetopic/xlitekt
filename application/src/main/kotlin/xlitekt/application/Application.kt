package xlitekt.application

import io.github.classgraph.ClassGraph
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.application.log
import io.ktor.server.engine.commandLineEnvironment
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.response.respondBytes
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import xlitekt.cache.cacheModule
import xlitekt.game.Game
import xlitekt.game.gameModule
import xlitekt.network.Network
import xlitekt.network.networkModule
import xlitekt.shared.lazy
import xlitekt.shared.sharedModule
import java.util.TimeZone
import kotlin.script.templates.standard.ScriptTemplateWithArgs
import kotlin.system.measureTimeMillis

/**
 * @author Jordan Abraham
 */
fun main(args: Array<String>) = commandLineEnvironment(args).start()

fun Application.module() {
    log.info("Starting XliteKt.")

    val time = measureTimeMillis {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
        addShutdownHook()
        installKoin()
        installKotlinScript()
        installHttpServer()
        lazy<Game>().start()
    }

    log.info(
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
            "      .'     `.  |   ||__|   |  |    `.             .'  |    \\  \\     |  |   \n" +
            "    .'  .'`.   `.'---'       |  '.'    `''-...... -'    |   |  \\  \\   |  '.' \n" +
            "  .'   /    `.   `.          |   /                      '    \\  \\  \\  |   /  \n" +
            " '----'       '----'         `'-'                      '------'  '---'`'-'   \n"
    )
    log.info("XliteKt launched in $time ms.")
    lazy<Network>().awaitOnPort(environment.config.property("ktor.deployment.port").getString().toInt())
}

val JavConfig: ByteArray = object {}::class.java.getResourceAsStream("/client_config/jav_config.ws")!!.readAllBytes()
val GamePack: ByteArray = object {}::class.java.getResourceAsStream("/client_config/gamepack.jar")!!.readAllBytes()
val BrowserControl0: ByteArray = object {}::class.java.getResourceAsStream("/client_config/browsercontrol_0.jar")?.readAllBytes() ?: byteArrayOf()
val BrowserControl1: ByteArray = object {}::class.java.getResourceAsStream("/client_config/browsercontrol_1.jar")?.readAllBytes() ?: byteArrayOf()

fun Application.installHttpServer() {
    embeddedServer(Netty, port = 8080) {
        routing {
            get("/jav_config.ws") {
                call.respondBytes(JavConfig)
            }
            get("/gamepack.jar") {
                call.respondBytes(GamePack)
            }
            get("/browsercontrol_0.jar") {
                if (BrowserControl0.isEmpty()) {
                    this@installHttpServer.log.warn("Missing browsercontrol_0.jar resource. Using empty response...")
                }
                call.respondBytes(BrowserControl0)
            }
            get("/browsercontrol_1.jar") {
                if (BrowserControl1.isEmpty()) {
                    this@installHttpServer.log.warn("Missing browsercontrol_1.jar resource. Using empty response...")
                }
                call.respondBytes(BrowserControl1)
            }
        }
    }.start(wait = false)
}

fun Application.installKoin() {
    install(Koin) {
        modules(
            module { single { this@installKoin.environment } },
            cacheModule,
            gameModule,
            networkModule,
            sharedModule
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

fun Application.addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(
        Thread {
            log.info("Running shutdown hook...")
            lazy<Network>().shutdown()
            lazy<Game>().shutdown()
        }
    )
}
