//[game](../../../index.md)/[xlitekt.game.actor.player](../index.md)/[Client](index.md)

# Client

[jvm]\
class [Client](index.md)(socket: Socket? = null, val readChannel: ByteReadChannel? = null, val writeChannel: ByteWriteChannel? = null)

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [Client](-client.md) | [jvm]<br>fun [Client](-client.md)(socket: Socket? = null, readChannel: ByteReadChannel? = null, writeChannel: ByteWriteChannel? = null) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [disconnect](disconnect.md) | [jvm]<br>fun [disconnect](disconnect.md)(reason: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)) |
| [handleException](handle-exception.md) | [jvm]<br>fun [handleException](handle-exception.md)(exception: [Exception](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-exception/index.html)) |
| [setIsaacCiphers](set-isaac-ciphers.md) | [jvm]<br>fun [setIsaacCiphers](set-isaac-ciphers.md)(clientCipher: ISAAC, serverCipher: ISAAC) |

## Properties

| Name | Summary |
|---|---|
| [clientCipher](client-cipher.md) | [jvm]<br>lateinit var [clientCipher](client-cipher.md): ISAAC |
| [logger](logger.md) | [jvm]<br>val [logger](logger.md): InlineLogger |
| [player](player.md) | [jvm]<br>lateinit var [player](player.md): [Player](../-player/index.md) |
| [readChannel](read-channel.md) | [jvm]<br>val [readChannel](read-channel.md): ByteReadChannel? = null |
| [seed](seed.md) | [jvm]<br>val [seed](seed.md): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [writeChannel](write-channel.md) | [jvm]<br>val [writeChannel](write-channel.md): ByteWriteChannel? = null |
