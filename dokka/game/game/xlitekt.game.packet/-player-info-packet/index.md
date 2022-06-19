//[game](../../../index.md)/[xlitekt.game.packet](../index.md)/[PlayerInfoPacket](index.md)

# PlayerInfoPacket

[jvm]\
data class [PlayerInfoPacket](index.md)(val players: NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt;, val viewport: [Viewport](../../xlitekt.game.actor.player/-viewport/index.md), val highDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, val lowDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, val alternativeHighDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, val alternativeLowDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, val movementStepsUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?&gt;) : [Packet](../-packet/index.md)

#### Author

Jordan Abraham

Tyler Telis

## Constructors

| | |
|---|---|
| [PlayerInfoPacket](-player-info-packet.md) | [jvm]<br>fun [PlayerInfoPacket](-player-info-packet.md)(players: NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt;, viewport: [Viewport](../../xlitekt.game.actor.player/-viewport/index.md), highDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, lowDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, alternativeHighDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, alternativeLowDefinitionUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt;, movementStepsUpdates: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?&gt;) |

## Properties

| Name | Summary |
|---|---|
| [alternativeHighDefinitionUpdates](alternative-high-definition-updates.md) | [jvm]<br>val [alternativeHighDefinitionUpdates](alternative-high-definition-updates.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt; |
| [alternativeLowDefinitionUpdates](alternative-low-definition-updates.md) | [jvm]<br>val [alternativeLowDefinitionUpdates](alternative-low-definition-updates.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt; |
| [highDefinitionUpdates](high-definition-updates.md) | [jvm]<br>val [highDefinitionUpdates](high-definition-updates.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt; |
| [lowDefinitionUpdates](low-definition-updates.md) | [jvm]<br>val [lowDefinitionUpdates](low-definition-updates.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[ByteArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte-array/index.html)?&gt; |
| [movementStepsUpdates](movement-steps-updates.md) | [jvm]<br>val [movementStepsUpdates](movement-steps-updates.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[MovementStep](../../xlitekt.game.actor.movement/-movement-step/index.md)?&gt; |
| [players](players.md) | [jvm]<br>val [players](players.md): NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt; |
| [viewport](viewport.md) | [jvm]<br>val [viewport](viewport.md): [Viewport](../../xlitekt.game.actor.player/-viewport/index.md) |
