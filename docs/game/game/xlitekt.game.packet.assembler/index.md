//[game](../../index.md)/[xlitekt.game.packet.assembler](index.md)

# Package xlitekt.game.packet.assembler

## Types

| Name | Summary |
|---|---|
| [PacketAssembler](-packet-assembler/index.md) | [jvm]<br>data class [PacketAssembler](-packet-assembler/index.md)(val opcode: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val size: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val packet: [Packet](../xlitekt.game.packet/-packet/index.md).([ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html)) -&gt; [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)) |
| [PacketAssemblerListener](-packet-assembler-listener/index.md) | [jvm]<br>class [PacketAssemblerListener](-packet-assembler-listener/index.md) : [MutableMap](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-mutable-map/index.html)&lt;[KClass](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.reflect/-k-class/index.html)&lt;*&gt;, [PacketAssembler](-packet-assembler/index.md)&gt; |
