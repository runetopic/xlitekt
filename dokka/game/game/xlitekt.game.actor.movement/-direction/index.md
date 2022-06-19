//[game](../../../index.md)/[xlitekt.game.actor.movement](../index.md)/[Direction](index.md)

# Direction

[jvm]\
@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)

value class [Direction](index.md)(val packedDirection: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [Direction](-direction.md) | [jvm]<br>fun [Direction](-direction.md)(dx: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), dz: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [Direction](-direction.md) | [jvm]<br>fun [Direction](-direction.md)(packedDirection: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Properties

| Name | Summary |
|---|---|
| [angle](angle.md) | [jvm]<br>val [angle](angle.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the angle associated with this direction. |
| [deltaX](delta-x.md) | [jvm]<br>val [deltaX](delta-x.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [deltaZ](delta-z.md) | [jvm]<br>val [deltaZ](delta-z.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [isFourPointCardinal](is-four-point-cardinal.md) | [jvm]<br>val [isFourPointCardinal](is-four-point-cardinal.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [opcodeForNPCDirection](opcode-for-n-p-c-direction.md) | [jvm]<br>val [opcodeForNPCDirection](opcode-for-n-p-c-direction.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the corresponding opcode used for this direction for npc movement. |
| [opcodeForPlayerDirection](opcode-for-player-direction.md) | [jvm]<br>val [opcodeForPlayerDirection](opcode-for-player-direction.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)<br>Returns the corresponding opcode used for this direction for player movement. |
| [packedDirection](packed-direction.md) | [jvm]<br>val [packedDirection](packed-direction.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
