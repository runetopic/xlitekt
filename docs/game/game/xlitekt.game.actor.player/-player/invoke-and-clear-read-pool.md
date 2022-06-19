//[game](../../../index.md)/[xlitekt.game.actor.player](../index.md)/[Player](index.md)/[invokeAndClearReadPool](invoke-and-clear-read-pool.md)

# invokeAndClearReadPool

[jvm]\
fun [invokeAndClearReadPool](invoke-and-clear-read-pool.md)(): [Unit](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-unit/index.html)?

Invokes and handles the pooled packets sent from the connected client. This is used to keep the player synchronized with the game loop no matter their actions from the client. The pool is then cleared after operation. This happens every tick.
