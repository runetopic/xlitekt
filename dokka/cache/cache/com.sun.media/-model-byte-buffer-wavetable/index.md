//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelByteBufferWavetable](index.md)

# ModelByteBufferWavetable

[jvm]\
open class [ModelByteBufferWavetable](index.md) : [ModelWavetable](../-model-wavetable/index.md)

Wavetable oscillator for pre-loaded data.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [ModelByteBufferWavetable](-model-byte-buffer-wavetable.md) | [jvm]<br>open fun [ModelByteBufferWavetable](-model-byte-buffer-wavetable.md)(buffer: [ModelByteBuffer](../-model-byte-buffer/index.md)) |
| [ModelByteBufferWavetable](-model-byte-buffer-wavetable.md) | [jvm]<br>open fun [ModelByteBufferWavetable](-model-byte-buffer-wavetable.md)(buffer: [ModelByteBuffer](../-model-byte-buffer/index.md), pitchcorrection: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)) |
| [ModelByteBufferWavetable](-model-byte-buffer-wavetable.md) | [jvm]<br>open fun [ModelByteBufferWavetable](-model-byte-buffer-wavetable.md)(buffer: [ModelByteBuffer](../-model-byte-buffer/index.md), format: [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html)) |
| [ModelByteBufferWavetable](-model-byte-buffer-wavetable.md) | [jvm]<br>open fun [ModelByteBufferWavetable](-model-byte-buffer-wavetable.md)(buffer: [ModelByteBuffer](../-model-byte-buffer/index.md), format: [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html), pitchcorrection: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [get8BitExtensionBuffer](get8-bit-extension-buffer.md) | [jvm]<br>open fun [get8BitExtensionBuffer](get8-bit-extension-buffer.md)(): [ModelByteBuffer](../-model-byte-buffer/index.md) |
| [getChannels](get-channels.md) | [jvm]<br>open fun [getChannels](get-channels.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [open](open.md) | [jvm]<br>open fun [open](open.md)(samplerate: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)): [ModelOscillatorStream](../-model-oscillator-stream/index.md) |
| [openStream](open-stream.md) | [jvm]<br>open fun [openStream](open-stream.md)(): [AudioFloatInputStream](../-audio-float-input-stream/index.md) |
| [set8BitExtensionBuffer](set8-bit-extension-buffer.md) | [jvm]<br>open fun [set8BitExtensionBuffer](set8-bit-extension-buffer.md)(buffer: [ModelByteBuffer](../-model-byte-buffer/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [attenuation](attenuation.md) | [jvm]<br>private open var [attenuation](attenuation.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [buffer](buffer.md) | [jvm]<br>private open val [buffer](buffer.md): [ModelByteBuffer](../-model-byte-buffer/index.md) |
| [format](format.md) | [jvm]<br>private open val [format](format.md): [AudioFormat](https://docs.oracle.com/javase/8/docs/api/javax/sound/sampled/AudioFormat.html) |
| [loopLength](loop-length.md) | [jvm]<br>private open var [loopLength](loop-length.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [loopStart](loop-start.md) | [jvm]<br>private open var [loopStart](loop-start.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [loopType](loop-type.md) | [jvm]<br>private open var [loopType](loop-type.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [pitchcorrection](pitchcorrection.md) | [jvm]<br>private open var [pitchcorrection](pitchcorrection.md): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
