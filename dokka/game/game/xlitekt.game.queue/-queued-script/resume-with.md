//[game](../../../index.md)/[xlitekt.game.queue](../index.md)/[QueuedScript](index.md)/[resumeWith](resume-with.md)

# resumeWith

[jvm]\
open override fun [resumeWith](resume-with.md)(result: [Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/index.html)&lt;[Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)&gt;)

The function that gets executed when we resume the queued script with the result. We set the current stage to null to allow for continuation. If an exception is caught within the result, we print it to the console.
