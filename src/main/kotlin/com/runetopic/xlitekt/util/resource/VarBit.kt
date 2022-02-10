package com.runetopic.xlitekt.util.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Tyler Telis
 */
@Serializable
data class VarBit(
    @SerialName("id")
    val id: Int,
    @SerialName("name")
    val name: String
)
