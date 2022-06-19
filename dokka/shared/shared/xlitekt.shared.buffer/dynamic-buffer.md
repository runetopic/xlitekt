//[shared](../../index.md)/[xlitekt.shared.buffer](index.md)/[dynamicBuffer](dynamic-buffer.md)

# dynamicBuffer

[jvm]\
inline fun [dynamicBuffer](dynamic-buffer.md)(block: BytePacketBuilder.() -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)): [ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)

#### Author

Jordan Abraham

Extension functions for the BytePacketBuilder class. These extension functions are only used for building small byte buffers that require dynamic allocation. These are not to be used for game packets.
