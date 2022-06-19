//[cache](../../../index.md)/[com.sun.media](../index.md)/[ModelByteBuffer](index.md)

# ModelByteBuffer

[jvm]\
open class [ModelByteBuffer](index.md)

This class is a pointer to a binary array either in memory or on disk.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [ModelByteBuffer](-model-byte-buffer.md) | [jvm]<br>open fun [ModelByteBuffer](-model-byte-buffer.md)(buffer: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;) |
| [ModelByteBuffer](-model-byte-buffer.md) | [jvm]<br>open fun [ModelByteBuffer](-model-byte-buffer.md)(buffer: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt;, offset: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html), len: [Int](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-int/index.html)) |
| [ModelByteBuffer](-model-byte-buffer.md) | [jvm]<br>open fun [ModelByteBuffer](-model-byte-buffer.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html)) |
| [ModelByteBuffer](-model-byte-buffer.md) | [jvm]<br>open fun [ModelByteBuffer](-model-byte-buffer.md)(file: [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html), offset: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), len: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)) |

## Functions

| Name | Summary |
|---|---|
| [array](array.md) | [jvm]<br>open fun [array](array.md)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Byte](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-byte/index.html)&gt; |
| [arrayOffset](array-offset.md) | [jvm]<br>open fun [arrayOffset](array-offset.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [capacity](capacity.md) | [jvm]<br>open fun [capacity](capacity.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [getFilePointer](get-file-pointer.md) | [jvm]<br>open fun [getFilePointer](get-file-pointer.md)(): [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html) |
| [getInputStream](get-input-stream.md) | [jvm]<br>open fun [getInputStream](get-input-stream.md)(): [InputStream](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html) |
| [load](load.md) | [jvm]<br>open fun [load](load.md)() |
| [loadAll](load-all.md) | [jvm]<br>open fun [loadAll](load-all.md)(col: [Collection](https://docs.oracle.com/javase/8/docs/api/java/util/Collection.html)&lt;[ModelByteBuffer](index.md)&gt;) |
| [subbuffer](subbuffer.md) | [jvm]<br>open fun [subbuffer](subbuffer.md)(beginIndex: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ModelByteBuffer](index.md)<br>open fun [subbuffer](subbuffer.md)(beginIndex: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), endIndex: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html)): [ModelByteBuffer](index.md)<br>open fun [subbuffer](subbuffer.md)(beginIndex: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), endIndex: [Long](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-long/index.html), independent: [Boolean](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-boolean/index.html)): [ModelByteBuffer](index.md) |
| [unload](unload.md) | [jvm]<br>open fun [unload](unload.md)() |
| [writeTo](write-to.md) | [jvm]<br>open fun [writeTo](write-to.md)(out: [OutputStream](https://docs.oracle.com/javase/8/docs/api/java/io/OutputStream.html)) |

## Properties

| Name | Summary |
|---|---|
| [file](file.md) | [jvm]<br>private open val [file](file.md): [File](https://docs.oracle.com/javase/8/docs/api/java/io/File.html) |
| [root](root.md) | [jvm]<br>private open val [root](root.md): [ModelByteBuffer](index.md) |
