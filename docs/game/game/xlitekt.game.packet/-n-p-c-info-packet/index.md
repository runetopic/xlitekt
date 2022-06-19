//[game](../../../index.md)/[xlitekt.game.packet](../index.md)/[NPCInfoPacket](index.md)

# NPCInfoPacket

[jvm]\
data class [NPCInfoPacket](index.md)(val viewport: [Viewport](../../xlitekt.game.actor.player/-viewport/index.md), val highDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, val movementStepsUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?&gt;) : [Packet](../-packet/index.md)

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [NPCInfoPacket](-n-p-c-info-packet.md) | [jvm]<br>fun [NPCInfoPacket](-n-p-c-info-packet.md)(viewport: [Viewport](../../xlitekt.game.actor.player/-viewport/index.md), highDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, movementStepsUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?&gt;) |

## Properties

| Name | Summary |
|---|---|
| [highDefinitionUpdates](high-definition-updates.md) | [jvm]<br>val [highDefinitionUpdates](high-definition-updates.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt; |
| [movementStepsUpdates](movement-steps-updates.md) | [jvm]<br>val [movementStepsUpdates](movement-steps-updates.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?&gt; |
| [viewport](viewport.md) | [jvm]<br>val [viewport](viewport.md): [Viewport](../../xlitekt.game.actor.player/-viewport/index.md) |
