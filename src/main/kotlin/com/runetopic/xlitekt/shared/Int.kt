package com.runetopic.xlitekt.shared

fun Int.packInterface(childId: Int = 0) = this shl 16 or childId
fun Int.packedToInterfaceId() = this shr 16
fun Int.packedToChildId() = this and 0xffff
fun Int.toBoolean() = this == 1
