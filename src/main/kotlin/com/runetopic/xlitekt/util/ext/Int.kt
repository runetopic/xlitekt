package com.runetopic.xlitekt.util.ext

fun Int.packInterface(component: Int = 0) = this.shl(16).or(component)
