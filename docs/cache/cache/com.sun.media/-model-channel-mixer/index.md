//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelChannelMixer](index.md)

# ModelChannelMixer

[jvm]\
interface [ModelChannelMixer](index.md) : [MidiChannel](https://docs.oracle.com/javase/8/docs/api/javax/sound/midi/MidiChannel.html)

ModelChannelMixer is used to process channel voice mix output before going to master output. It can be used to:

- Implement non-voice oriented instruments.
- Add insert effect to instruments; for example distortion effect.
- **Warning! Classes that implements ModelChannelMixer must be thread-safe.**

#### Author

Karl Helgason

## Functions

| Name | Summary |
|---|---|
| [allNotesOff](index.md#-445626222%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [allNotesOff](index.md#-445626222%2FFunctions%2F-82533025)() |
| [allSoundOff](index.md#166945248%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [allSoundOff](index.md#166945248%2FFunctions%2F-82533025)() |
| [controlChange](index.md#1143964019%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [controlChange](index.md#1143964019%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [getChannelPressure](index.md#-548165721%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getChannelPressure](index.md#-548165721%2FFunctions%2F-82533025)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [getController](index.md#-583313020%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getController](index.md#-583313020%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [getMono](index.md#-1543685592%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getMono](index.md#-1543685592%2FFunctions%2F-82533025)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getMute](index.md#-1907586126%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getMute](index.md#-1907586126%2FFunctions%2F-82533025)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getOmni](index.md#165872050%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getOmni](index.md#165872050%2FFunctions%2F-82533025)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [getPitchBend](index.md#-1613787626%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getPitchBend](index.md#-1613787626%2FFunctions%2F-82533025)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [getPolyPressure](index.md#1244122649%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getPolyPressure](index.md#1244122649%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [getProgram](index.md#1103140619%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getProgram](index.md#1103140619%2FFunctions%2F-82533025)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [getSolo](index.md#691696556%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [getSolo](index.md#691696556%2FFunctions%2F-82533025)(): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [localControl](index.md#158912211%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [localControl](index.md#158912211%2FFunctions%2F-82533025)(p: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [noteOff](index.md#-1742466269%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [noteOff](index.md#-1742466269%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [noteOn](index.md#-141271745%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [noteOn](index.md#-141271745%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [process](process.md) | [jvm]<br>abstract fun [process](process.md)(buffer: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)&gt;&gt;, offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)): [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html) |
| [programChange](index.md#270406214%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [programChange](index.md#270406214%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [resetAllControllers](index.md#808391132%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [resetAllControllers](index.md#808391132%2FFunctions%2F-82533025)() |
| [setChannelPressure](index.md#-382228640%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [setChannelPressure](index.md#-382228640%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [setMono](index.md#627735550%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [setMono](index.md#627735550%2FFunctions%2F-82533025)(p: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setMute](index.md#315636532%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [setMute](index.md#315636532%2FFunctions%2F-82533025)(p: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setOmni](index.md#1294533940%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [setOmni](index.md#1294533940%2FFunctions%2F-82533025)(p: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [setPitchBend](index.md#-546759279%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [setPitchBend](index.md#-546759279%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [setPolyPressure](index.md#-677016903%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [setPolyPressure](index.md#-677016903%2FFunctions%2F-82533025)(p: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), p1: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [setSolo](index.md#1688618490%2FFunctions%2F-82533025) | [jvm]<br>abstract fun [setSolo](index.md#1688618490%2FFunctions%2F-82533025)(p: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [stop](stop.md) | [jvm]<br>abstract fun [stop](stop.md)() |
