package com.runetopic.xlitekt.util.resource

import com.runetopic.xlitekt.plugin.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

fun loadAllMapSquares(): List<MapSquare> = Json.decodeFromStream(MapSquare::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.xteas.path").getString())!!)
