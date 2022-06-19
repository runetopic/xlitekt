//[game](../../index.md)/[xlitekt.game.actor.player](index.md)

# Package xlitekt.game.actor.player

## Types

| Name | Summary |
|---|---|
| [Client](-client/index.md) | [jvm]<br>class [Client](-client/index.md)(socket: Socket? = null, val readChannel: ByteReadChannel? = null, val writeChannel: ByteWriteChannel? = null) |
| [Player](-player/index.md) | [jvm]<br>class [Player](-player/index.md)(var location: [Location](../xlitekt.game.world.map/-location/index.md) = Location(3222, 3222), val username: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val password: [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html), val rights: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) = 0, var weight: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) = 0.0f, val appearance: [Render.Appearance](../xlitekt.game.actor.render/-render/-appearance/index.md) = Render.Appearance().also { it.displayName = username }, var runEnergy: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html), var brandNew: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = true, val skills: [Skills](../xlitekt.game.content.skill/-skills/index.md) = Skills()) : [Actor](../xlitekt.game.actor/-actor/index.md) |
| [PlayerDecoder](-player-decoder/index.md) | [jvm]<br>object [PlayerDecoder](-player-decoder/index.md) |
| [Viewport](-viewport/index.md) | [jvm]<br>class [Viewport](-viewport/index.md)(val player: [Player](-player/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [addExperience](add-experience.md) | [jvm]<br>fun [Player](-player/index.md).[addExperience](add-experience.md)(skill: [Skill](../xlitekt.game.content.skill/-skill/index.md), experience: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [drainRunEnergy](drain-run-energy.md) | [jvm]<br>fun [Player](-player/index.md).[drainRunEnergy](drain-run-energy.md)() |
| [message](message.md) | [jvm]<br>inline fun [Player](-player/index.md).[message](message.md)(message: () -&gt; [String](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-string/index.html)): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [process](process.md) | [jvm]<br>fun [Player](-player/index.md).[process](process.md)() |
| [rebuildNormal](rebuild-normal.md) | [jvm]<br>inline fun [Player](-player/index.md).[rebuildNormal](rebuild-normal.md)(players: NonBlockingHashMapLong&lt;[Player](-player/index.md)&gt;, update: () -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [renderAppearance](render-appearance.md) | [jvm]<br>fun [Player](-player/index.md).[renderAppearance](render-appearance.md)() |
| [resetMiniMapFlag](reset-mini-map-flag.md) | [jvm]<br>fun [Player](-player/index.md).[resetMiniMapFlag](reset-mini-map-flag.md)(): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [restoreRunEnergy](restore-run-energy.md) | [jvm]<br>fun [Player](-player/index.md).[restoreRunEnergy](restore-run-energy.md)() |
| [script](script.md) | [jvm]<br>fun [Player](-player/index.md).[script](script.md)(scriptId: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), vararg parameters: [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [updateRunEnergy](update-run-energy.md) | [jvm]<br>fun [Player](-player/index.md).[updateRunEnergy](update-run-energy.md)(): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)? |
| [updateStat](update-stat.md) | [jvm]<br>fun [Player](-player/index.md).[updateStat](update-stat.md)(skill: [Skill](../xlitekt.game.content.skill/-skill/index.md), level: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), experience: [Double](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-double/index.html)) |
| [varp](varp.md) | [jvm]<br>inline fun [Player](-player/index.md).[varp](varp.md)(id: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), value: () -&gt; [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
