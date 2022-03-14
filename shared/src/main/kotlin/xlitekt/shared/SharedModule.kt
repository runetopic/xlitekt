package xlitekt.shared

import org.koin.dsl.module
import xlitekt.shared.config.JavaConfig
import xlitekt.shared.resource.Resource.interfaceInfoResource
import xlitekt.shared.resource.Resource.mapSquaresResource
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
    single { JavaConfig() }
}
