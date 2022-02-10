package com.runetopic.xlitekt.util.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Jordan Abraham
 */
@Serializable
data class Sequence(
    @SerialName("name")
    val name: String,
    @SerialName("id")
    val id: Int
)
