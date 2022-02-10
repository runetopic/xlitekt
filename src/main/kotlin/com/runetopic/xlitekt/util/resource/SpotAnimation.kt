package com.runetopic.xlitekt.util.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @author Jordan Abraham
 */
@Serializable
data class SpotAnimation(
    @SerialName("name")
    val name: String,
    @SerialName("id")
    val id: Int
)
