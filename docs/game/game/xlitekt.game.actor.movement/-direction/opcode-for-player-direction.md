//[game](../../../index.md)/[xlitekt.game.actor.movement](../index.md)/[Direction](index.md)/[opcodeForPlayerDirection](opcode-for-player-direction.md)

# opcodeForPlayerDirection

[jvm]\
val [opcodeForPlayerDirection](opcode-for-player-direction.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)

Returns the corresponding opcode used for this direction for player movement.

## Parameters

jvm

| | |
|---|---|
| useSixteenPoints | Walking uses eight point cardinal direction and running uses sixteen point cardinal direction.<br>Running direction opcodes.<br>| 121415 | | ####10 | | ####08 | | ####06 | | 010304 |<br>Walking direction opcodes.<br>| 0607 | | XX04 | | 0102 | |
