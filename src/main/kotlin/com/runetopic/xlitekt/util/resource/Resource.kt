package com.runetopic.xlitekt.util.resource

import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class MapSquares(list: List<MapSquare>) : ArrayList<MapSquare>(list)
class Sequences(list: List<Sequence>) : HashMap<String, Sequence>(list.associateBy(Sequence::name))
class SpotAnimations(list: List<SpotAnimation>) : HashMap<String, SpotAnimation>(list.associateBy(SpotAnimation::name))
class Varps(list: List<VarPlayer>) : HashMap<String, VarPlayer>(list.associateBy(VarPlayer::name))
class VarBits(list: List<VarBit>) : HashMap<String, VarBit>(list.associateBy(VarBit::name))

fun mapSquaresResource(): MapSquares = MapSquares(
    Json.decodeFromStream(
        MapSquare::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.xteas").getString()
        )!!
    )
)

fun sequencesResource(): Sequences = Sequences(
    Json.decodeFromStream(
        Sequence::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.sequences").getString()
        )!!
    )
)

fun spotAnimationsResource(): SpotAnimations = SpotAnimations(
    Json.decodeFromStream(
        SpotAnimation::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.spotanimations").getString()
        )!!
    )
)

fun varpsResource(): Varps = Varps(
    Json.decodeFromStream(
        VarBit::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.varps").getString()
        )!!
    )
)

fun varBitsResource(): VarBits = VarBits(
    Json.decodeFromStream(
        VarBit::class.java.getResourceAsStream(
            inject<ApplicationEnvironment>().value.config.property("game.resources.varbits").getString()
        )!!
    )
)
