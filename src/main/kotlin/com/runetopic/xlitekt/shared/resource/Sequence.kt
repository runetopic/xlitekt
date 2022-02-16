package com.runetopic.xlitekt.shared.resource

import kotlinx.serialization.Serializable

/**
 * @author Jordan Abraham
 */
@Serializable
data class Sequence(
    val name: String,
    val id: Int
)
