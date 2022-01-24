package com.runetopic.xlitekt.util.resource

import com.runetopic.xlitekt.game.map.MapSquare
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

fun loadAllMapSquares(): List<MapSquare> = Json.decodeFromStream(MapSquare::class.java.getResourceAsStream("/xteas202.json")!!)
