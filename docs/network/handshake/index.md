## Handshake
There are two types of handshakes opcodes the client sends to the server.


| Opcode | Usage                                   |
|--------|-----------------------------------------|
| 15     | JS5 (Used to request latest game files) |
| 14     | Login                                   |

The client must send a handshake request with an opcode of 15 to start the process. 

This lets the server know the client is attempting to connect to download the game files, if any updates require downloading. If the connection succeeds the server will respond with an opcode of 0 to let the client know to proceed with JS5.

The actual decoding might look something like this (This is built on top of Ktor):
```kotlin
override suspend fun read(client: Client): ReadEvent.HandshakeReadEvent {
    if (client.readChannel.availableForRead < 4) {
        withTimeout(environment.config.property("network.timeout").getString().toLong()) { client.readChannel.awaitContent() }
    }
    return when (val opcode = client.readChannel.readByte().toInt()) {
        HANDSHAKE_JS5_OPCODE -> ReadEvent.HandshakeReadEvent(opcode, client.readChannel.readInt())
        HANDSHAKE_LOGIN_OPCODE -> ReadEvent.HandshakeReadEvent(opcode)
        else -> throw IllegalStateException("Unhandled opcode found during client/server handshake. Opcode=$opcode")
    }
}
```

The code above waits until at least 4 bytes are available to read from the channel, if we exceed the amount of time specific in our application.conf, we can go ahead and disconnect the client.
