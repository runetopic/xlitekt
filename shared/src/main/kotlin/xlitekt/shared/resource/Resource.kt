package xlitekt.shared.resource

import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import xlitekt.shared.inject

class MapSquares(list: List<MapSquareResource>) : ArrayList<MapSquareResource>(list)
class Sequences(list: Map<String, SequenceResource>) : HashMap<String, SequenceResource>(list)
class SpotAnimations(list: Map<String, SpotAnimationResource>) : HashMap<String, SpotAnimationResource>(list)
class Varps(list: Map<String, VarInfoResource>) : HashMap<String, VarInfoResource>(list)
class VarBits(list: Map<String, VarInfoResource>) : HashMap<String, VarInfoResource>(list)
class InterfaceInfoMap(list: Map<String, InterfaceInfoResource>) : HashMap<String, InterfaceInfoResource>(list)

object Resource {
    fun mapSquaresResource(): MapSquares = MapSquares(loadResource("game.resources.xteas"))
    fun sequencesResource(): Sequences = Sequences(loadResource<List<SequenceResource>>("game.resources.sequences").associateBy(SequenceResource::name))
    fun spotAnimationsResource(): SpotAnimations = SpotAnimations(loadResource<List<SpotAnimationResource>>("game.resources.spot_animations").associateBy(SpotAnimationResource::name))
    fun varpsResource(): Varps = Varps(loadResource<List<VarInfoResource>>("game.resources.varps").associateBy(VarInfoResource::name))
    fun varBitsResource(): VarBits = VarBits(loadResource<List<VarInfoResource>>("game.resources.varbits").associateBy(VarInfoResource::name))
    fun interfaceInfoResource(): InterfaceInfoMap = InterfaceInfoMap(loadResource<List<InterfaceInfoResource>>("game.resources.interface_info").associateBy(InterfaceInfoResource::name))

    private inline fun <reified T> loadResource(path: String): T =
        Json.decodeFromStream(Resource::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property(path).getString())!!)
}
