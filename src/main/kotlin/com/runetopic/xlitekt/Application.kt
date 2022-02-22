package com.runetopic.xlitekt

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.Game
import com.runetopic.xlitekt.network.Network
import com.runetopic.xlitekt.plugin.koin.installKoin
import com.runetopic.xlitekt.plugin.script.installKotlinScript
import io.ktor.application.Application
import io.ktor.server.engine.commandLineEnvironment
import org.koin.core.context.stopKoin
import org.koin.ktor.ext.get
import java.util.TimeZone
import kotlin.system.measureTimeMillis

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
    logger.debug { "XliteKt launched in $time ms." }
    get<Network>().awaitOnPort(environment.config.property("ktor.deployment.port").getString().toInt())
}

fun Application.addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(
        Thread {
            logger.debug { "Running shutdown hook..." }
            get<Game>().shutdownGracefully()
            get<Network>().shutdownGracefully()
            logger.debug { "Stopping koin..." }
            stopKoin()
        }
    )
}
