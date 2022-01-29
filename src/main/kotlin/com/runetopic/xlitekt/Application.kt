package com.runetopic.xlitekt

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.Game
import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.HitBarType
import com.runetopic.xlitekt.game.actor.render.HitType
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.Network
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.plugin.ktor.installKoin
import io.ktor.application.Application
import io.ktor.server.engine.commandLineEnvironment
import org.koin.core.context.stopKoin
import org.koin.ktor.ext.get
import java.util.TimeZone

private val logger = InlineLogger()

fun main(args: Array<String>) = commandLineEnvironment(args).start()

fun Application.module() {
    addShutdownHook()
    logger.info { "Starting XliteKt." }
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    installKoin()
    get<Game>().start()
    val npc = NPC(10, Tile(3220, 3220))
    npc.overheadChat("What it do slick?")
    npc.faceTile(Tile(3222, 3222))
    npc.hit(HitBarType.DEFAULT, null, HitType.VENOM_DAMAGE, 5, 0)
    npc.spotAnimation(Render.SpotAnimation(320))
    inject<World>().value.npcs.register(npc)
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
