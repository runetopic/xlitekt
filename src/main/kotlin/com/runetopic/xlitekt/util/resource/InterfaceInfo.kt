package com.runetopic.xlitekt.util.resource

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
        val resizableModernChildId: Int,
        val fullScreenChildId: Int = -1
    ) {
        fun childIdForLayout(currentInterfaceLayout: InterfaceLayout): Int {
            return when (currentInterfaceLayout) {
                InterfaceLayout.FIXED -> fixedChildId
                InterfaceLayout.RESIZABLE -> resizableChildId
                InterfaceLayout.RESIZABLE_MODERN -> resizableModernChildId
                InterfaceLayout.FULL_SCREEN -> fullScreenChildId
            }
        }
    }
}
