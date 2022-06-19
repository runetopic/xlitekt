//[game](../../../index.md)/[xlitekt.game.packet](../index.md)/[MessageGamePacket](index.md)

# MessageGamePacket

[jvm]\
data class [MessageGamePacket](index.md)(val type: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), val message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val hasPrefix: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, val prefix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;&quot;) : [Packet](../-packet/index.md)

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [MessageGamePacket](-message-game-packet.md) | [jvm]<br>fun [MessageGamePacket](-message-game-packet.md)(type: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), message: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), hasPrefix: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false, prefix: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) = &quot;&quot;) |

## Properties

| Name | Summary |
|---|---|
| [hasPrefix](has-prefix.md) | [jvm]<br>val [hasPrefix](has-prefix.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false |
| [message](message.md) | [jvm]<br>val [message](message.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [prefix](prefix.md) | [jvm]<br>val [prefix](prefix.md): [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html) |
| [type](type.md) | [jvm]<br>val [type](type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
