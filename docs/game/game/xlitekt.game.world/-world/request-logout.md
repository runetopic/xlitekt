//[game](../../../index.md)/[xlitekt.game.world](../index.md)/[World](index.md)/[requestLogout](request-logout.md)

# requestLogout

[jvm]\
fun [requestLogout](request-logout.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md))

Request logout out the game world. Only use this for logging out a player. This is because logout requests are synchronized with the game tick and this is how logout requests are pooled.

Logout requests are processed by the WorldLogoutSynchronizer.
