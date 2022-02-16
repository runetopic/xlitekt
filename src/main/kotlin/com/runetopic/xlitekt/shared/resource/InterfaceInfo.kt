package com.runetopic.xlitekt.shared.resource

import com.runetopic.xlitekt.game.ui.InterfaceLayout
import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class InterfaceInfo(
    val id: Int,
    val name: String,
    val destination: IfDestination,
) {
    @Serializable
    data class IfDestination(
        val fixedChildId: Int,
        val resizableChildId: Int,
        val resizableModernChildId: Int
    ) {
        fun childIdForLayout(currentInterfaceLayout: InterfaceLayout): Int = when (currentInterfaceLayout) {
            InterfaceLayout.FIXED -> fixedChildId
            InterfaceLayout.RESIZABLE -> resizableChildId
            InterfaceLayout.RESIZABLE_MODERN -> resizableModernChildId
        }
    }
}
