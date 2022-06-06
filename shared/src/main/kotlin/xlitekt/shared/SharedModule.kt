package xlitekt.shared

import org.koin.dsl.module
import xlitekt.shared.resource.Resource.examineItemResource
import xlitekt.shared.resource.Resource.examineNPCResource
import xlitekt.shared.resource.Resource.examineObjectResource
import xlitekt.shared.resource.Resource.interfaceInfoResource
import xlitekt.shared.resource.Resource.itemInfoResource
import xlitekt.shared.resource.Resource.mapSquaresResource
import xlitekt.shared.resource.Resource.npcInfoResource
import xlitekt.shared.resource.Resource.prayerInfoResource
import xlitekt.shared.resource.Resource.sequencesResource
import xlitekt.shared.resource.Resource.spotAnimationsResource
import xlitekt.shared.resource.Resource.varBitsResource
import xlitekt.shared.resource.Resource.varpsResource

/**
 * @author Jordan Abraham
 */
val sharedModule = module(true) {
    single { mapSquaresResource() }
    single { sequencesResource() }
    single { spotAnimationsResource() }
    single { varpsResource() }
    single { varBitsResource() }
    single { interfaceInfoResource() }
    single { npcInfoResource() }
    single { examineNPCResource() }
    single { examineObjectResource() }
    single { examineItemResource() }
    single { itemInfoResource() }
    single { prayerInfoResource() }
}
