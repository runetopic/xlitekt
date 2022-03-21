package xlitekt.shared.resource

import io.ktor.application.ApplicationEnvironment
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import xlitekt.shared.inject

class MapSquares(list: Map<Int, MapSquareResource>) : HashMap<Int, MapSquareResource>(list)
class Sequences(list: Map<String, SequenceResource>) : HashMap<String, SequenceResource>(list)
class SpotAnimations(list: Map<String, SpotAnimationResource>) : HashMap<String, SpotAnimationResource>(list)
class Varps(list: Map<String, VarInfoResource>) : HashMap<String, VarInfoResource>(list)
class VarBits(list: Map<String, VarInfoResource>) : HashMap<String, VarInfoResource>(list)
class InterfaceInfoMap(list: Map<String, InterfaceInfoResource>) : HashMap<String, InterfaceInfoResource>(list)
class NPCSpawns(list: List<NPCSpawnsResource>) : ArrayList<NPCSpawnsResource>(list)

object Resource {
    private val json = Json { allowStructuredMapKeys = true; ignoreUnknownKeys = true }

    fun mapSquaresResource(): MapSquares = MapSquares(loadResource<List<MapSquareResource>>("game.resources.xteas").associateBy(MapSquareResource::mapsquare))
    fun sequencesResource(): Sequences = Sequences(loadResource<List<SequenceResource>>("game.resources.sequences").associateBy(SequenceResource::name))
    fun spotAnimationsResource(): SpotAnimations = SpotAnimations(loadResource<List<SpotAnimationResource>>("game.resources.spot_animations").associateBy(SpotAnimationResource::name))
    fun varpsResource(): Varps = Varps(loadResource<List<VarInfoResource>>("game.resources.varps").associateBy(VarInfoResource::name))
    fun varBitsResource(): VarBits = VarBits(loadResource<List<VarInfoResource>>("game.resources.varbits").associateBy(VarInfoResource::name))
    fun interfaceInfoResource(): InterfaceInfoMap = InterfaceInfoMap(loadResource<List<InterfaceInfoResource>>("game.resources.interface_info").associateBy(InterfaceInfoResource::name))
    fun npcInfoResource(): NPCSpawns = NPCSpawns(loadResource("game.resources.npc_info"))

    private inline fun <reified T> loadResource(path: String): T =
        json.decodeFromStream(Resource::class.java.getResourceAsStream(inject<ApplicationEnvironment>().value.config.property(path).getString())!!)
}
