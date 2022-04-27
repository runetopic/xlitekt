package xlitekt.cache.provider

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
open class EntryType(@Transient open val id: Int = -1)
