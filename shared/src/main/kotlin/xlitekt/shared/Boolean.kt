package xlitekt.shared

/**
 * @author Jordan Abraham
 */
fun Boolean.toInt(): Int = if (this) 1 else 0
fun Boolean.toByte(): Byte = if (this) 1 else 0
fun Boolean.toIntInv(): Int = if (this) 0 else 1
