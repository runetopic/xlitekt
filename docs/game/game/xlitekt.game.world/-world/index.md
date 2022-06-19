//[game](../../../index.md)/[xlitekt.game.world](../index.md)/[World](index.md)

# World

[jvm]\
class [World](index.md)(players: [PlayerList](../../xlitekt.game.actor/index.md#1509214331%2FClasslikes%2F440369633) = PlayerList(MAX_PLAYERS), npcs: [NPCList](../../xlitekt.game.actor/index.md#282877961%2FClasslikes%2F440369633) = NPCList(MAX_NPCS), val loginRequests: [ConcurrentMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentMap.html)&lt;[Player](../../xlitekt.game.actor.player/-player/index.md), [Client](../../xlitekt.game.actor.player/-client/index.md)&gt; = ConcurrentHashMap(), val logoutRequests: [ConcurrentHashMap.KeySetView](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.KeySetView.html)&lt;[Player](../../xlitekt.game.actor.player/-player/index.md), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; = ConcurrentHashMap.newKeySet())

## Constructors

| | |
|---|---|
| [World](-world.md) | [jvm]<br>fun [World](-world.md)(players: [PlayerList](../../xlitekt.game.actor/index.md#1509214331%2FClasslikes%2F440369633) = PlayerList(MAX_PLAYERS), npcs: [NPCList](../../xlitekt.game.actor/index.md#282877961%2FClasslikes%2F440369633) = NPCList(MAX_NPCS), loginRequests: [ConcurrentMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentMap.html)&lt;[Player](../../xlitekt.game.actor.player/-player/index.md), [Client](../../xlitekt.game.actor.player/-client/index.md)&gt; = ConcurrentHashMap(), logoutRequests: [ConcurrentHashMap.KeySetView](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.KeySetView.html)&lt;[Player](../../xlitekt.game.actor.player/-player/index.md), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; = ConcurrentHashMap.newKeySet()) |

## Types

| Name | Summary |
|---|---|
| [Companion](-companion/index.md) | [jvm]<br>object [Companion](-companion/index.md) |

## Functions

| Name | Summary |
|---|---|
| [addPlayer](add-player.md) | [jvm]<br>fun [addPlayer](add-player.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [npcs](npcs.md) | [jvm]<br>fun [npcs](npcs.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md)&gt; |
| [players](players.md) | [jvm]<br>fun [players](players.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt; |
| [playersMapped](players-mapped.md) | [jvm]<br>fun [playersMapped](players-mapped.md)(): NonBlockingHashMapLong&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt; |
| [removePlayer](remove-player.md) | [jvm]<br>fun [removePlayer](remove-player.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [requestLogin](request-login.md) | [jvm]<br>fun [requestLogin](request-login.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md), client: [Client](../../xlitekt.game.actor.player/-client/index.md))<br>Request login into the game world. Only use this for logging in a player. This is because login requests are synchronized with the game tick and this is how login requests are pooled. |
| [requestLogout](request-logout.md) | [jvm]<br>fun [requestLogout](request-logout.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md))<br>Request logout out the game world. Only use this for logging out a player. This is because logout requests are synchronized with the game tick and this is how logout requests are pooled. |
| [spawn](spawn.md) | [jvm]<br>fun [spawn](spawn.md)(npc: [NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md))<br>Spawn a npc into the game world and update the corresponding zone. |

## Properties

| Name | Summary |
|---|---|
| [loginRequests](login-requests.md) | [jvm]<br>val [loginRequests](login-requests.md): [ConcurrentMap](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentMap.html)&lt;[Player](../../xlitekt.game.actor.player/-player/index.md), [Client](../../xlitekt.game.actor.player/-client/index.md)&gt; |
| [logoutRequests](logout-requests.md) | [jvm]<br>val [logoutRequests](logout-requests.md): [ConcurrentHashMap.KeySetView](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ConcurrentHashMap.KeySetView.html)&lt;[Player](../../xlitekt.game.actor.player/-player/index.md), [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)&gt; |
