package com.runetopic.xlitekt.util.resource

import com.runetopic.xlitekt.game.map.MapSquare
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

fun loadAllMapSquares(): List<MapSquare> = Json.decodeFromStream(MapSquare::class.java.getResourceAsStream("/map/xteas${inject<ApplicationEnvironment>().value.config.property("game.build.major").getString()}.json")!!)
