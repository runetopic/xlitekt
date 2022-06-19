//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelOscillatorStream](index.md)

# ModelOscillatorStream

[jvm]\
interface [ModelOscillatorStream](index.md)

This interface is used for audio streams from ModelOscillator.

#### Author

Karl Helgason

## Functions

| Name | Summary |
|---|---|
| [close](close.md) | [jvm]<br>abstract fun [close](close.md)() |
| [noteOff](note-off.md) | [jvm]<br>abstract fun [noteOff](note-off.md)(velocity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [noteOn](note-on.md) | [jvm]<br>abstract fun [noteOn](note-on.md)(channel: [MidiChannel](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiChannel.html), voice: [VoiceStatus](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/VoiceStatus.html), noteNumber: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), velocity: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [read](read.md) | [jvm]<br>abstract fun [read](read.md)(buffer: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)&gt;&gt;, offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [setPitch](set-pitch.md) | [jvm]<br>abstract fun [setPitch](set-pitch.md)(pitch: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)) |
