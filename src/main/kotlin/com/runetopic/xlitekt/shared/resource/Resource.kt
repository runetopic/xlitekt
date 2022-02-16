package com.runetopic.xlitekt.shared.resource

import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream

class MapSquares(list: List<MapSquare>) : ArrayList<MapSquare>(list)
class Sequences(list: Map<String, Sequence>) : HashMap<String, Sequence>(list)
class SpotAnimations(list: Map<String, SpotAnimation>) : HashMap<String, SpotAnimation>(list)
class Varps(list: Map<String, VarPlayer>) : HashMap<String, VarPlayer>(list)
class VarBits(list: Map<String, VarBit>) : HashMap<String, VarBit>(list)
class InterfaceInfoMap(list: Map<String, InterfaceInfo>) : HashMap<String, InterfaceInfo>(list)

object Resource {
    fun mapSquaresResource(): MapSquares = MapSquares(loadResource("game.resources.xteas"))
    fun sequencesResource(): Sequences = Sequences(loadResource<List<Sequence>>("game.resources.sequences").associateBy(Sequence::name))
    fun spotAnimationsResource(): SpotAnimations = SpotAnimations(loadResource<List<SpotAnimation>>("game.resources.spot_animations").associateBy(SpotAnimation::name))
    fun varpsResource(): Varps = Varps(loadResource<List<VarPlayer>>("game.resources.varps").associateBy(VarPlayer::name))
    fun varBitsResource(): VarBits = VarBits(loadResource<List<VarBit>>("game.resources.varbits").associateBy(VarBit::name))
    fun interfaceInfoResource(): InterfaceInfoMap = InterfaceInfoMap(loadResource<List<InterfaceInfo>>("game.resources.interface_info").associateBy(InterfaceInfo::name))

    private inline fun <reified T> loadResource(path: String): T =
        Json.decodeFromStream(Resource::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property(path).getString())!!)
}
