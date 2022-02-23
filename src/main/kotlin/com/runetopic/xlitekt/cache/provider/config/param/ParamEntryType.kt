package com.runetopic.xlitekt.cache.provider.config.param

import com.runetopic.xlitekt.cache.provider.EntryType
import com.runetopic.xlitekt.cache.provider.config.ScriptType

/**
 * @author Jordan Abraham
 */
data class ParamEntryType(
    override val id: Int,
    var type: ScriptType? = null,
    var defaultInt: Int = 0,
    var autoDisable: Boolean = true,
    var defaultString: String = ""
) : EntryType(id)
