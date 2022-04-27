package xlitekt.cache.provider.config.enum

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.config.ScriptType

@Serializable
data class EnumEntryType(
    override val id: Int,
    var keyType: ScriptType? = null,
    var valType: ScriptType? = null,
    var defaultString: String = "null",
    var defaultInt: Int = 0,
    var size: Int = 0,
    var params: Map<Int, @Contextual Any> = mapOf()
) : EntryType(id)
