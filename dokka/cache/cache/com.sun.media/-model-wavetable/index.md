//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelWavetable](index.md)

# ModelWavetable

[jvm]\
interface [ModelWavetable](index.md) : [ModelOscillator](../-model-oscillator/index.md)

This is a wavetable oscillator interface.

#### Author

Karl Helgason

## Functions

| Name | Summary |
|---|---|
| [getAttenuation](../-model-oscillator/get-attenuation.md) | [jvm]<br>abstract fun [getAttenuation](../-model-oscillator/get-attenuation.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)<br>Attenuation is in cB. |
| [getChannels](../-model-oscillator/get-channels.md) | [jvm]<br>abstract fun [getChannels](../-model-oscillator/get-channels.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [getLoopLength](get-loop-length.md) | [jvm]<br>abstract fun [getLoopLength](get-loop-length.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getLoopStart](get-loop-start.md) | [jvm]<br>abstract fun [getLoopStart](get-loop-start.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [getLoopType](get-loop-type.md) | [jvm]<br>abstract fun [getLoopType](get-loop-type.md)(): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [getPitchcorrection](get-pitchcorrection.md) | [jvm]<br>abstract fun [getPitchcorrection](get-pitchcorrection.md)(): [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html) |
| [open](../-model-oscillator/open.md) | [jvm]<br>abstract fun [open](../-model-oscillator/open.md)(samplerate: [Float](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-float/index.html)): [ModelOscillatorStream](../-model-oscillator-stream/index.md) |
| [openStream](open-stream.md) | [jvm]<br>abstract fun [openStream](open-stream.md)(): [AudioFloatInputStream](../-audio-float-input-stream/index.md) |

## Properties

| Name | Summary |
|---|---|
| [LOOP_TYPE_FORWARD](-l-o-o-p_-t-y-p-e_-f-o-r-w-a-r-d.md) | [jvm]<br>val [LOOP_TYPE_FORWARD](-l-o-o-p_-t-y-p-e_-f-o-r-w-a-r-d.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LOOP_TYPE_OFF](-l-o-o-p_-t-y-p-e_-o-f-f.md) | [jvm]<br>val [LOOP_TYPE_OFF](-l-o-o-p_-t-y-p-e_-o-f-f.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LOOP_TYPE_PINGPONG](-l-o-o-p_-t-y-p-e_-p-i-n-g-p-o-n-g.md) | [jvm]<br>val [LOOP_TYPE_PINGPONG](-l-o-o-p_-t-y-p-e_-p-i-n-g-p-o-n-g.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LOOP_TYPE_RELEASE](-l-o-o-p_-t-y-p-e_-r-e-l-e-a-s-e.md) | [jvm]<br>val [LOOP_TYPE_RELEASE](-l-o-o-p_-t-y-p-e_-r-e-l-e-a-s-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |
| [LOOP_TYPE_REVERSE](-l-o-o-p_-t-y-p-e_-r-e-v-e-r-s-e.md) | [jvm]<br>val [LOOP_TYPE_REVERSE](-l-o-o-p_-t-y-p-e_-r-e-v-e-r-s-e.md): [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html) |

## Inheritors

| Name |
|---|
| [ModelByteBufferWavetable](../-model-byte-buffer-wavetable/index.md) |
