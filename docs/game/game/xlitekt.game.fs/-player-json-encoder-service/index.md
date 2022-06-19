//[game](../../../index.md)/[xlitekt.game.fs](../index.md)/[PlayerJsonEncoderService](index.md)

# PlayerJsonEncoderService

[jvm]\
class [PlayerJsonEncoderService](index.md)(executor: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html)) : [Runnable](https://docs.oracle.com/javase/8/docs/api/java/lang/Runnable.html)

#### Author

Jordan Abraham

## Constructors

| | |
|---|---|
| [PlayerJsonEncoderService](-player-json-encoder-service.md) | [jvm]<br>fun [PlayerJsonEncoderService](-player-json-encoder-service.md)(executor: [ScheduledExecutorService](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledExecutorService.html)) |

## Functions

| Name | Summary |
|---|---|
| [requestSave](request-save.md) | [jvm]<br>fun [requestSave](request-save.md)(player: [Player](../../xlitekt.game.actor.player/-player/index.md)) |
| [run](run.md) | [jvm]<br>open override fun [run](run.md)() |
| [shutdown](shutdown.md) | [jvm]<br>fun [shutdown](shutdown.md)() |
| [start](start.md) | [jvm]<br>fun [start](start.md)(): [ScheduledFuture](https://docs.oracle.com/javase/8/docs/api/java/util/concurrent/ScheduledFuture.html)&lt;*&gt; |
