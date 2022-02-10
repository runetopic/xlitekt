package com.runetopic.xlitekt.util.resource

import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

fun loadAllMapSquares(): List<MapSquare> = Json.decodeFromStream(MapSquare::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.xteas").getString())!!)
fun loadAllSequences(): List<Sequence> = Json.decodeFromStream(Sequence::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.sequences").getString())!!)
fun loadAllSpotAnimations(): List<SpotAnimation> = Json.decodeFromStream(SpotAnimation::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.spotanimations").getString())!!)
fun loadAllVarps(): List<VarPlayer> = Json.decodeFromStream(VarPlayer::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.varps").getString())!!)
fun loadAllVarbits(): Map<String, Int> {
    val stream = VarBit::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.varbits").getString())!!
    val list = Json.decodeFromStream<List<VarBit>>(stream)

    val map = mutableMapOf<String, Int>()
    list.forEach {
        map[it.name] = it.id
    }

    return map
}
