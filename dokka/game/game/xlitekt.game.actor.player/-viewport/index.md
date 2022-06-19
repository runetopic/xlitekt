//[game](../../../index.md)/[xlitekt.game.actor.player](../index.md)/[Viewport](index.md)

# Viewport

[jvm]\
class [Viewport](index.md)(val player: [Player](../-player/index.md))

## Constructors

| | |
|---|---|
| [Viewport](-viewport.md) | [jvm]<br>fun [Viewport](-viewport.md)(player: [Player](../-player/index.md)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [equals](equals.md) | [jvm]<br>open operator override fun [equals](equals.md)(other: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)?): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [hashCode](hash-code.md) | [jvm]<br>open override fun [hashCode](hash-code.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [init](init.md) | [jvm]<br>fun [init](init.md)(builder: [ByteBuffer](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html), players: NonBlockingHashMapLong&lt;[Player](../-player/index.md)&gt;) |
| [isNsn](is-nsn.md) | [jvm]<br>fun [isNsn](is-nsn.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [resize](resize.md) | [jvm]<br>fun [resize](resize.md)() |
| [setNsn](set-nsn.md) | [jvm]<br>fun [setNsn](set-nsn.md)(index: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [update](update.md) | [jvm]<br>fun [update](update.md)() |

## Properties

| Name | Summary |
|---|---|
| [forceViewDistance](force-view-distance.md) | [jvm]<br>var [forceViewDistance](force-view-distance.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [highDefinitions](high-definitions.md) | [jvm]<br>val [highDefinitions](high-definitions.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [highDefinitionsCount](high-definitions-count.md) | [jvm]<br>var [highDefinitionsCount](high-definitions-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [locations](locations.md) | [jvm]<br>val [locations](locations.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [lowDefinitions](low-definitions.md) | [jvm]<br>val [lowDefinitions](low-definitions.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [lowDefinitionsCount](low-definitions-count.md) | [jvm]<br>var [lowDefinitionsCount](low-definitions-count.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0 |
| [npcs](npcs.md) | [jvm]<br>val [npcs](npcs.md): [ArrayList](https://docs.oracle.com/javase/8/docs/api/java/util/ArrayList.html)&lt;[NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md)&gt; |
| [nsnFlags](nsn-flags.md) | [jvm]<br>val [nsnFlags](nsn-flags.md): [IntArray](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int-array/index.html) |
| [player](player.md) | [jvm]<br>val [player](player.md): [Player](../-player/index.md) |
| [players](players.md) | [jvm]<br>val [players](players.md): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Player](../-player/index.md)?&gt; |
| [viewDistance](view-distance.md) | [jvm]<br>var [viewDistance](view-distance.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
