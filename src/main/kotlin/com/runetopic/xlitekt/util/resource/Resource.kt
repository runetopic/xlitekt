package com.runetopic.xlitekt.util.resource

import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

fun loadAllMapSquares(): List<MapSquare> = Json.decodeFromStream(MapSquare::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property("game.resources.xteas").getString())!!)

fun loadAllSequences(): Sequences = Json.decodeFromStream<List<Sequence>>(
    Sequence::class.java.getResourceAsStream(
        inject<ApplicationEnvironment>().value.config.property("game.resources.sequences").getString()
    )!!
).associateBy { it.name }

fun loadAllSpotAnimations(): SpotAnimations = Json.decodeFromStream<List<SpotAnimation>>(
    SpotAnimation::class.java.getResourceAsStream(
        inject<ApplicationEnvironment>().value.config.property("game.resources.spotanimations").getString()
    )!!
).associateBy { it.name }

fun loadAllVarps(): Varps = Json.decodeFromStream<List<VarPlayer>>(
    VarBit::class.java.getResourceAsStream(
        inject<ApplicationEnvironment>().value.config.property("game.resources.varps").getString()
    )!!
).associateBy { it.name }

fun loadAllVarBits(): VarBits = Json.decodeFromStream<List<VarBit>>(
    VarBit::class.java.getResourceAsStream(
        inject<ApplicationEnvironment>().value.config.property("game.resources.varbits").getString()
    )!!
).associateBy { it.name }

typealias Sequences = Map<String, Sequence>
typealias SpotAnimations = Map<String, SpotAnimation>
typealias Varps = Map<String, VarPlayer>
typealias VarBits = Map<String, VarBit>
