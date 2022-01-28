package com.runetopic.xlitekt

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.Game
import com.runetopic.xlitekt.network.Network
import com.runetopic.xlitekt.plugin.ktor.installKoin
import io.ktor.application.Application
import io.ktor.server.engine.commandLineEnvironment
import org.koin.core.context.stopKoin
import org.koin.ktor.ext.get
import java.util.TimeZone
import kotlin.system.exitProcess

private val logger = InlineLogger()

fun main(args: Array<String>) = commandLineEnvironment(args).start()

fun Application.module() {
    addShutdownHook()
    logger.info { "Starting XliteKt." }
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    installKoin()
    get<Game>().start()
    get<Network>().awaitOnPort(environment.config.property("ktor.deployment.port").getString().toInt())
    logger.info { "Main thread reaches end." }
}

fun Application.addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(Thread {
        logger.info { "Shutting down" }
        get<Game>().shutdownGracefully()
        get<Network>().shutdownGracefully()
        stopKoin()
    })
}
