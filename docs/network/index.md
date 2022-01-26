## Network

The network pipeline for XliteKt consist of a few moving parts:

- HTTP (This is used for serving client configuration files along with the latest gamepack.jar)
- JS5 (This is used for serving the cache files to the client)
- [Handshake](handshake/index.md) (This is used to establish a connection between the client and server and to determine the connection type)
- Login (This is used for processing incomming login request from the client)
- Game (This is used for processing and sending outgoing packets to and from the client)
