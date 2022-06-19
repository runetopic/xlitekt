//[game](../../../index.md)/[xlitekt.game.queue](../index.md)/[QueuedScriptConditions](index.md)

# QueuedScriptConditions

[jvm]\
sealed class [QueuedScriptConditions](index.md)

This class represents the possible script conditions.

#### Author

Tyler Telis

## Types

| Name | Summary |
|---|---|
| [LocationCondition](-location-condition/index.md) | [jvm]<br>class [LocationCondition](-location-condition/index.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md), destination: [Location](../../xlitekt.game.world.map/-location/index.md)) : [QueuedScriptConditions](index.md) |
| [PredicateCondition](-predicate-condition/index.md) | [jvm]<br>class [PredicateCondition](-predicate-condition/index.md)(predicate: () -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) : [QueuedScriptConditions](index.md) |
| [WaitCondition](-wait-condition/index.md) | [jvm]<br>class [WaitCondition](-wait-condition/index.md)(ticks: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) : [QueuedScriptConditions](index.md) |

## Functions

| Name | Summary |
|---|---|
| [resume](resume.md) | [jvm]<br>abstract fun [resume](resume.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |

## Inheritors

| Name |
|---|
| [WaitCondition](-wait-condition/index.md) |
| [LocationCondition](-location-condition/index.md) |
| [PredicateCondition](-predicate-condition/index.md) |
