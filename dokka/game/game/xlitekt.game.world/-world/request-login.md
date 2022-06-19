//[game](../../../index.md)/[xlitekt.game.world](../index.md)/[World](index.md)/[requestLogin](request-login.md)

# requestLogin

[jvm]\
fun [requestLogin](request-login.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md), client: [Client](../../xlitekt.game.actor.player/-client/index.md))

Request login into the game world. Only use this for logging in a player. This is because login requests are synchronized with the game tick and this is how login requests are pooled.

Login requests are processed by the WorldLoginSynchronizer.
