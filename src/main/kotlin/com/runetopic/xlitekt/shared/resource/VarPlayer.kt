package com.runetopic.xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class VarPlayer(
    val id: Int,
    val name: String
)
