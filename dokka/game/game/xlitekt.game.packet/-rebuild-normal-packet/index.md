//[game](../../../index.md)/[xlitekt.game.packet](../index.md)/[RebuildNormalPacket](index.md)

# RebuildNormalPacket

[jvm]\
data class [RebuildNormalPacket](index.md)(val viewport: [Viewport](../../xlitekt.game.actor.player/-viewport/index.md), val location: [Location](../../xlitekt.game.world.map/-location/index.md), val initialize: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), val players: NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt;) : [Packet](../-packet/index.md)

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [RebuildNormalPacket](-rebuild-normal-packet.md) | [jvm]<br>fun [RebuildNormalPacket](-rebuild-normal-packet.md)(viewport: [Viewport](../../xlitekt.game.actor.player/-viewport/index.md), location: [Location](../../xlitekt.game.world.map/-location/index.md), initialize: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), players: NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [initialize](initialize.md) | [jvm]<br>val [initialize](initialize.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [location](location.md) | [jvm]<br>val [location](location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [players](players.md) | [jvm]<br>val [players](players.md): NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt; |
| [viewport](viewport.md) | [jvm]<br>val [viewport](viewport.md): [Viewport](../../xlitekt.game.actor.player/-viewport/index.md) |
