package com.runetopic.xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class InterfaceInfoResource(
    val id: Int,
    val name: String,
    val resizableChildId: Int
)
