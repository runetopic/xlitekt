//[game](../../../index.md)/[xlitekt.game.world.map.zone](../index.md)/[Zone](index.md)

# Zone

[jvm]\
class [Zone](index.md)(val location: [Location](../../xlitekt.game.world.map/-location/index.md), val players: NonBlockingHashSet&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt; = NonBlockingHashSet(), val npcs: NonBlockingHashSet&lt;[NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md)&gt; = NonBlockingHashSet(), locs: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[GameObject](../../xlitekt.game.world.map/-game-object/index.md)&gt; = HashSet(64), val locsSpawned: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[GameObject](../../xlitekt.game.world.map/-game-object/index.md)&gt; = HashSet(), val objsSpawned: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[FloorItem](../../xlitekt.game.content.item/-floor-item/index.md)&gt; = HashSet())

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [Zone](-zone.md) | [jvm]<br>fun [Zone](-zone.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md), players: NonBlockingHashSet&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt; = NonBlockingHashSet(), npcs: NonBlockingHashSet&lt;[NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md)&gt; = NonBlockingHashSet(), locs: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[GameObject](../../xlitekt.game.world.map/-game-object/index.md)&gt; = HashSet(64), locsSpawned: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[GameObject](../../xlitekt.game.world.map/-game-object/index.md)&gt; = HashSet(), objsSpawned: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[FloorItem](../../xlitekt.game.content.item/-floor-item/index.md)&gt; = HashSet()) |

## Functions

| Name | Summary |
|---|---|
| [active](active.md) | [jvm]<br>fun [active](active.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if this zone is active or not. |
| [enterZone](enter-zone.md) | [jvm]<br>fun [enterZone](enter-zone.md)(actor: [Actor](../../xlitekt.game.actor/-actor/index.md))<br>Make an actor enter this zone. |
| [finalizeUpdateRequests](finalize-update-requests.md) | [jvm]<br>fun [finalizeUpdateRequests](finalize-update-requests.md)()<br>Finalizes this zone update requests. Updates corresponding collections in this zone and clears the update requests. |
| [invokeUpdateRequests](invoke-update-requests.md) | [jvm]<br>fun [invokeUpdateRequests](invoke-update-requests.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md)): [Zone](index.md)<br>Updates a player with updates about this zone. This happens every tick. |
| [leaveZone](leave-zone.md) | [jvm]<br>fun [leaveZone](leave-zone.md)(actor: [Actor](../../xlitekt.game.actor/-actor/index.md), nextZone: [Zone](index.md)? = null)<br>Make an actor leave this zone. The actor is immediately required to enter a new zone upon leaving. nextZone is nullable for logout. |
| [neighboringLocs](neighboring-locs.md) | [jvm]<br>fun [neighboringLocs](neighboring-locs.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[GameObject](../../xlitekt.game.world.map/-game-object/index.md)&gt;<br>Returns a list of game objects that are inside this zone and neighboring zones. By default, the range is limited to a standard 7x7 build area. |
| [neighboringNpcs](neighboring-npcs.md) | [jvm]<br>fun [neighboringNpcs](neighboring-npcs.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md)&gt;<br>Returns a list of npcs that are inside this zone and neighboring zones. By default, the range is limited to a 7x7 area since by default npcs are only visible within 15 tiles. |
| [neighboringObjs](neighboring-objs.md) | [jvm]<br>fun [neighboringObjs](neighboring-objs.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[FloorItem](../../xlitekt.game.content.item/-floor-item/index.md)&gt;<br>Returns a list of floor items that are inside this zone and neighboring zones. By default, the range is limited to a standard 7x7 build area. |
| [neighboringPlayers](neighboring-players.md) | [jvm]<br>fun [neighboringPlayers](neighboring-players.md)(): [List](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-list/index.html)&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt;<br>Returns a list of players that are inside this zone and neighboring zones. By default, the range is limited to a standard 7x7 build area. |
| [requestAddLoc](request-add-loc.md) | [jvm]<br>fun [requestAddLoc](request-add-loc.md)(gameObject: [GameObject](../../xlitekt.game.world.map/-game-object/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Requests a game object to be added to this zone. Returns true if this game object is able to be added. |
| [requestAddMapProjAnim](request-add-map-proj-anim.md) | [jvm]<br>fun [requestAddMapProjAnim](request-add-map-proj-anim.md)(projectile: [Projectile](../../xlitekt.game.content.projectile/-projectile/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Requests a map proj anim to be added to this zone. Returns true if this map proj anim is able to be added. |
| [requestAddObj](request-add-obj.md) | [jvm]<br>fun [requestAddObj](request-add-obj.md)(floorItem: [FloorItem](../../xlitekt.game.content.item/-floor-item/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Requests a floor item to be added to this zone. Returns true if this floor item is able to be added. |
| [requestRemoveLoc](request-remove-loc.md) | [jvm]<br>fun [requestRemoveLoc](request-remove-loc.md)(gameObject: [GameObject](../../xlitekt.game.world.map/-game-object/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Requests a game object to be removed from this zone. Returns true if this game object is able to be removed. Returns false if this game object is already requested to be removed. |
| [requestRemoveObj](request-remove-obj.md) | [jvm]<br>fun [requestRemoveObj](request-remove-obj.md)(floorItem: [FloorItem](../../xlitekt.game.content.item/-floor-item/index.md)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Requests a floor item to be removed from this zone. Returns true if this floor item is able to be removed. Returns false if this floor item is already requested to be removed. |
| [updating](updating.md) | [jvm]<br>fun [updating](updating.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if this zone needs updating or not. |

## Properties

| Name | Summary |
|---|---|
| [location](location.md) | [jvm]<br>val [location](location.md): [Location](../../xlitekt.game.world.map/-location/index.md) |
| [locsSpawned](locs-spawned.md) | [jvm]<br>val [locsSpawned](locs-spawned.md): [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[GameObject](../../xlitekt.game.world.map/-game-object/index.md)&gt; |
| [npcs](npcs.md) | [jvm]<br>val [npcs](npcs.md): NonBlockingHashSet&lt;[NPC](../../xlitekt.game.actor.npc/-n-p-c/index.md)&gt; |
| [objsSpawned](objs-spawned.md) | [jvm]<br>val [objsSpawned](objs-spawned.md): [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[FloorItem](../../xlitekt.game.content.item/-floor-item/index.md)&gt; |
| [players](players.md) | [jvm]<br>val [players](players.md): NonBlockingHashSet&lt;[Player](../../xlitekt.game.actor.player/-player/index.md)&gt; |
