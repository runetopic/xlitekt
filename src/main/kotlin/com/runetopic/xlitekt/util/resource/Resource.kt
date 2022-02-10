package com.runetopic.xlitekt.util.resource

import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

fun loadAllMapSquares(): List<MapSquare> = Json.decodeFromStream(MapSquare::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.xteas").getString())!!)
fun loadAllSequences(): List<Sequence> = Json.decodeFromStream(Sequence::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.sequences").getString())!!)
fun loadAllSpotAnimations(): List<SpotAnimation> = Json.decodeFromStream(SpotAnimation::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.spotanimations").getString())!!)
