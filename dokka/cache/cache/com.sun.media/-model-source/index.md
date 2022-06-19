//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelSource](index.md)

# ModelSource

[jvm]\
open class [ModelSource](index.md)

This class is used to identify sources in connection blocks, see ModelConnectionBlock.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [ModelSource](-model-source.md) | [jvm]<br>open fun [ModelSource](-model-source.md)() |
| [ModelSource](-model-source.md) | [jvm]<br>open fun [ModelSource](-model-source.md)(id: [ModelIdentifier](../-model-identifier/index.md)) |
| [ModelSource](-model-source.md) | [jvm]<br>open fun [ModelSource](-model-source.md)(id: [ModelIdentifier](../-model-identifier/index.md), direction: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [ModelSource](-model-source.md) | [jvm]<br>open fun [ModelSource](-model-source.md)(id: [ModelIdentifier](../-model-identifier/index.md), direction: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), polarity: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)) |
| [ModelSource](-model-source.md) | [jvm]<br>open fun [ModelSource](-model-source.md)(id: [ModelIdentifier](../-model-identifier/index.md), direction: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), polarity: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html), transform: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [ModelSource](-model-source.md) | [jvm]<br>open fun [ModelSource](-model-source.md)(id: [ModelIdentifier](../-model-identifier/index.md), transform: [ModelTransform](../-model-transform/index.md)) |

## Functions

| Name | Summary |
|---|---|
| [getIdentifier](get-identifier.md) | [jvm]<br>open fun [getIdentifier](get-identifier.md)(): [ModelIdentifier](../-model-identifier/index.md) |
| [setIdentifier](set-identifier.md) | [jvm]<br>open fun [setIdentifier](set-identifier.md)(source: [ModelIdentifier](../-model-identifier/index.md)) |

## Properties

| Name | Summary |
|---|---|
| [SOURCE_EG1](-s-o-u-r-c-e_-e-g1.md) | [jvm]<br>val [SOURCE_EG1](-s-o-u-r-c-e_-e-g1.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_EG2](-s-o-u-r-c-e_-e-g2.md) | [jvm]<br>val [SOURCE_EG2](-s-o-u-r-c-e_-e-g2.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_LFO1](-s-o-u-r-c-e_-l-f-o1.md) | [jvm]<br>val [SOURCE_LFO1](-s-o-u-r-c-e_-l-f-o1.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_LFO2](-s-o-u-r-c-e_-l-f-o2.md) | [jvm]<br>val [SOURCE_LFO2](-s-o-u-r-c-e_-l-f-o2.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_MIDI_CC_0](-s-o-u-r-c-e_-m-i-d-i_-c-c_0.md) | [jvm]<br>val [SOURCE_MIDI_CC_0](-s-o-u-r-c-e_-m-i-d-i_-c-c_0.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_MIDI_CHANNEL_PRESSURE](-s-o-u-r-c-e_-m-i-d-i_-c-h-a-n-n-e-l_-p-r-e-s-s-u-r-e.md) | [jvm]<br>val [SOURCE_MIDI_CHANNEL_PRESSURE](-s-o-u-r-c-e_-m-i-d-i_-c-h-a-n-n-e-l_-p-r-e-s-s-u-r-e.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_MIDI_PITCH](-s-o-u-r-c-e_-m-i-d-i_-p-i-t-c-h.md) | [jvm]<br>val [SOURCE_MIDI_PITCH](-s-o-u-r-c-e_-m-i-d-i_-p-i-t-c-h.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_MIDI_POLY_PRESSURE](-s-o-u-r-c-e_-m-i-d-i_-p-o-l-y_-p-r-e-s-s-u-r-e.md) | [jvm]<br>val [SOURCE_MIDI_POLY_PRESSURE](-s-o-u-r-c-e_-m-i-d-i_-p-o-l-y_-p-r-e-s-s-u-r-e.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_MIDI_RPN_0](-s-o-u-r-c-e_-m-i-d-i_-r-p-n_0.md) | [jvm]<br>val [SOURCE_MIDI_RPN_0](-s-o-u-r-c-e_-m-i-d-i_-r-p-n_0.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_NONE](-s-o-u-r-c-e_-n-o-n-e.md) | [jvm]<br>val [SOURCE_NONE](-s-o-u-r-c-e_-n-o-n-e.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_NOTEON_KEYNUMBER](-s-o-u-r-c-e_-n-o-t-e-o-n_-k-e-y-n-u-m-b-e-r.md) | [jvm]<br>val [SOURCE_NOTEON_KEYNUMBER](-s-o-u-r-c-e_-n-o-t-e-o-n_-k-e-y-n-u-m-b-e-r.md): [ModelIdentifier](../-model-identifier/index.md) |
| [SOURCE_NOTEON_VELOCITY](-s-o-u-r-c-e_-n-o-t-e-o-n_-v-e-l-o-c-i-t-y.md) | [jvm]<br>val [SOURCE_NOTEON_VELOCITY](-s-o-u-r-c-e_-n-o-t-e-o-n_-v-e-l-o-c-i-t-y.md): [ModelIdentifier](../-model-identifier/index.md) |
| [transform](transform.md) | [jvm]<br>private open var [transform](transform.md): [ModelTransform](../-model-transform/index.md) |
