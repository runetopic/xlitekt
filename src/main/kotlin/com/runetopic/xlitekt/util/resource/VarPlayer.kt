package com.runetopic.xlitekt.util.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class VarPlayer(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
