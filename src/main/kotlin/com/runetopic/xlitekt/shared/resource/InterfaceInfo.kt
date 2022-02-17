package com.runetopic.xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class InterfaceInfo(
    val id: Int,
    val name: String,
    val resizableChildId: Int? = null,
)
