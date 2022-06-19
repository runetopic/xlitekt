//[game](../../index.md)/[xlitekt.game.world.map.zone](index.md)

# Package xlitekt.game.world.map.zone

## Types

| Name | Summary |
|---|---|
| [LocalLocation](-local-location/index.md) | [jvm]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [LocalLocation](-local-location/index.md)(val packedLocation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [Zone](-zone/index.md) | [jvm]<br>class [Zone](-zone/index.md)(val location: [Location](../xlitekt.game.world.map/-location/index.md), val players: NonBlockingHashSet&lt;[Player](../xlitekt.game.actor.player/-player/index.md)&gt; = NonBlockingHashSet(), val npcs: NonBlockingHashSet&lt;[NPC](../xlitekt.game.actor.npc/-n-p-c/index.md)&gt; = NonBlockingHashSet(), locs: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[GameObject](../xlitekt.game.world.map/-game-object/index.md)&gt; = HashSet(64), val locsSpawned: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[GameObject](../xlitekt.game.world.map/-game-object/index.md)&gt; = HashSet(), val objsSpawned: [HashSet](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.collections/-hash-set/index.html)&lt;[FloorItem](../xlitekt.game.content.item/-floor-item/index.md)&gt; = HashSet()) |
| [ZoneLocation](-zone-location/index.md) | [jvm]<br>@[JvmInline](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.jvm/-jvm-inline/index.html)<br>value class [ZoneLocation](-zone-location/index.md)(val packedLocation: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [Zones](-zones/index.md) | [jvm]<br>class [Zones](-zones/index.md) |

## Functions

| Name | Summary |
|---|---|
| [transform](transform.md) | [jvm]<br>fun [ZoneLocation](-zone-location/index.md).[transform](transform.md)(deltaX: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), deltaZ: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), deltaLevel: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0): [ZoneLocation](-zone-location/index.md) |
