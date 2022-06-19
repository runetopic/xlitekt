//[game](../../../index.md)/[xlitekt.game.queue](../index.md)/[QueuedScript](index.md)

# QueuedScript

[jvm]\
data class [QueuedScript](index.md)&lt;[T](index.md) : [Actor](../../xlitekt.game.actor/-actor/index.md)&gt;(executor: [T](index.md), val priority: [QueuedScriptPriority](../-queued-script-priority/index.md)) : [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt; 

#### Author

Tyler Telis

Tom (Got some inspiration and coroutine ideas from rsmod)

## Constructors

| | |
|---|---|
| [QueuedScript](-queued-script.md) | [jvm]<br>fun &lt;[T](index.md) : [Actor](../../xlitekt.game.actor/-actor/index.md)&gt; [QueuedScript](-queued-script.md)(executor: [T](index.md), priority: [QueuedScriptPriority](../-queued-script-priority/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [execute](execute.md) | [jvm]<br>fun [execute](execute.md)()<br>Marks the script as executed and resumes our execution. |
| [resumeWith](resume-with.md) | [jvm]<br>open override fun [resumeWith](resume-with.md)(result: [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;)<br>The function that gets executed when we resume the queued script with the result. We set the current stage to null to allow for continuation. If an exception is caught within the result, we print it to the console. |
| [suspended](suspended.md) | [jvm]<br>fun [suspended](suspended.md)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)<br>Returns if the queued script is suspended. E.g: the script is suspended when the stage is null |
| [terminate](terminate.md) | [jvm]<br>fun [terminate](terminate.md)()<br>Terminates the queued script and invokes the termination script. |
| [wait](wait.md) | [jvm]<br>suspend fun [wait](wait.md)(ticks: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html))<br>This function sets the current stage to a WaitCondition to allow the script to wait for a specified number of ticks. |
| [waitForInput](wait-for-input.md) | [jvm]<br>suspend fun [waitForInput](wait-for-input.md)()<br>This function sets the current stage to a PredicateCondition, that checks if the input is not null and waits for it. This is used for dialogues/input dialogues that require waiting for user input. |
| [waitForLocation](wait-for-location.md) | [jvm]<br>suspend fun [waitForLocation](wait-for-location.md)(location: [Location](../../xlitekt.game.world.map/-location/index.md))<br>This function sets the current stage to a LocationCondition to allow the script to wait until the location matches. |
| [waitUntil](wait-until.md) | [jvm]<br>suspend fun [waitUntil](wait-until.md)(predicate: () -&gt; [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html))<br>This function sets the current stage to a PredicateCondition to allow the script to wait until the predicate matches. |

## Properties

| Name | Summary |
|---|---|
| [context](context.md) | [jvm]<br>open override val [context](context.md): [CoroutineContext](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-coroutine-context/index.html)<br>This is the coroutine's context, which starts off as an empty context for each script. |
| [continuation](continuation.md) | [jvm]<br>lateinit var [continuation](continuation.md): [Continuation](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin.coroutines/-continuation/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;<br>The continuation unit to invoke whe we need to resume this script. |
| [executed](executed.md) | [jvm]<br>var [executed](executed.md): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) = false<br>If the script has been exectuted or not. |
| [input](input.md) | [jvm]<br>var [input](input.md): [Any](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-any/index.html)? = null<br>Any input value that the script has been waiting for |
| [priority](priority.md) | [jvm]<br>val [priority](priority.md): [QueuedScriptPriority](../-queued-script-priority/index.md) |
| [terminationScript](termination-script.md) | [jvm]<br>var [terminationScript](termination-script.md): [QueuedScriptUnit](../index.md#-1567077484%2FClasslikes%2F440369633)&lt;[T](index.md)&gt;? = null<br>The termination script to exectute upon terminating the queued script. |
