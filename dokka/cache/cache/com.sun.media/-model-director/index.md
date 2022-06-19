//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelDirector](index.md)

# ModelDirector

[jvm]\
interface [ModelDirector](index.md)

A director chooses what performers should be played for each note on and note off events. ModelInstrument can implement custom performer who chooses what performers to play for example by sustain pedal is off or on. The default director (ModelStandardDirector) chooses performers by there keyfrom,keyto,velfrom,velto properties.

#### Author

Karl Helgason

## Functions

| Name | Summary |
|---|---|
| [close](close.md) | [jvm]<br>abstract fun [close](close.md)() |
| [noteOff](note-off.md) | [jvm]<br>abstract fun [noteOff](note-off.md)(noteNumber: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), velocity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [noteOn](note-on.md) | [jvm]<br>abstract fun [noteOn](note-on.md)(noteNumber: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), velocity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |

## Inheritors

| Name |
|---|
| [ModelStandardDirector](../-model-standard-director/index.md) |
| [ModelStandardIndexedDirector](../-model-standard-indexed-director/index.md) |
