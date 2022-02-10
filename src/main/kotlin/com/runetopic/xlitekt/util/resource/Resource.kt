package com.runetopic.xlitekt.util.resource

import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

fun loadAllMapSquares(): List<MapSquare> = Json.decodeFromStream(MapSquare::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.xteas").getString())!!)

fun loadAllSequences(): Sequences = Sequences(
    Json.decodeFromStream(
        Sequence::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.sequences").getString()
        )!!
    )
)

fun loadAllSpotAnimations(): SpotAnimations = SpotAnimations(
    Json.decodeFromStream(
        SpotAnimation::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.spotanimations").getString()
        )!!
    )
)

fun loadAllVarps(): Varps = Varps(
    Json.decodeFromStream(
        VarBit::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.varps").getString()
        )!!
    )
)

fun loadAllVarBits(): VarBits = VarBits(
    Json.decodeFromStream(
        VarBit::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.varbits").getString()
        )!!
    )
)

class Sequences(list: List<Sequence>) : HashMap<String, Sequence>(list.associateBy { it.name })
class SpotAnimations(list: List<SpotAnimation>) : HashMap<String, SpotAnimation>(list.associateBy { it.name })
class Varps(list: List<VarPlayer>) : HashMap<String, VarPlayer>(list.associateBy { it.name })
class VarBits(list: List<VarBit>) : HashMap<String, VarBit>(list.associateBy { it.name })
