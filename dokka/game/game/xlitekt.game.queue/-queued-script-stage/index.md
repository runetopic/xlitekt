//[game](../../../index.md)/[xlitekt.game.queue](../index.md)/[QueuedScriptStage](index.md)

# QueuedScriptStage

[jvm]\
data class [QueuedScriptStage](index.md)(val condition: [QueuedScriptConditions](../-queued-script-conditions/index.md), val continuation: [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;)

This class represents the queued scripts current stage.

#### Author

Tyler Telis

## Constructors

| | |
|---|---|
| [QueuedScriptStage](-queued-script-stage.md) | [jvm]<br>fun [QueuedScriptStage](-queued-script-stage.md)(condition: [QueuedScriptConditions](../-queued-script-conditions/index.md), continuation: [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;) |

## Properties

| Name | Summary |
|---|---|
| [condition](condition.md) | [jvm]<br>val [condition](condition.md): [QueuedScriptConditions](../-queued-script-conditions/index.md) |
| [continuation](continuation.md) | [jvm]<br>val [continuation](continuation.md): [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; |
