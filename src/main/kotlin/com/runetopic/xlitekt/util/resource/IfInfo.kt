package com.runetopic.xlitekt.util.resource

import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class IfInfo(
    val id: Int,
    val name: String,
    val destination: IfDestination
) {
    @Serializable
    data class IfDestination(
        val fixedChildId: Int,
        val resizableChildId: Int,
        val resizableModernChildId: Int
    )
}
