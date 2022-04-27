package xlitekt.cache.provider.config.param

import kotlinx.serialization.Serializable
import xlitekt.cache.provider.EntryType
import xlitekt.cache.provider.config.ScriptType

/**
 * @author Jordan Abraham
 */
@Serializable
data class ParamEntryType(
    override val id: Int,
    var type: ScriptType? = null,
    var defaultInt: Int = 0,
    var autoDisable: Boolean = true,
    var defaultString: String = ""
) : EntryType(id)
