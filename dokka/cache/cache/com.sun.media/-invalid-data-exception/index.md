//[cache](../../../index.md)/[com.sun.media](../index.md)/[InvalidDataException](index.md)

# InvalidDataException

[jvm]\
open class [InvalidDataException](index.md) : [IOException](https://docs.oracle.com/javase/8/docs/api/java/io/IOException.html)

This exception is used when a file contains illegal or unexpected data.

#### Author

Karl Helgason

## Constructors

| | |
|---|---|
| [InvalidDataException](-invalid-data-exception.md) | [jvm]<br>open fun [InvalidDataException](-invalid-data-exception.md)() |
| [InvalidDataException](-invalid-data-exception.md) | [jvm]<br>open fun [InvalidDataException](-invalid-data-exception.md)(s: [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html)) |

## Functions

| Name | Summary |
|---|---|
| [addSuppressed](index.md#-1898257014%2FFunctions%2F-82533025) | [jvm]<br>fun [addSuppressed](index.md#-1898257014%2FFunctions%2F-82533025)(exception: [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html)) |
| [fillInStackTrace](index.md#-1207709164%2FFunctions%2F-82533025) | [jvm]<br>open fun [fillInStackTrace](index.md#-1207709164%2FFunctions%2F-82533025)(): [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html) |
| [getCause](index.md#-252564762%2FFunctions%2F-82533025) | [jvm]<br>open fun [getCause](index.md#-252564762%2FFunctions%2F-82533025)(): [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html) |
| [getLocalizedMessage](index.md#-2138642817%2FFunctions%2F-82533025) | [jvm]<br>open fun [getLocalizedMessage](index.md#-2138642817%2FFunctions%2F-82533025)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getMessage](index.md#1068546184%2FFunctions%2F-82533025) | [jvm]<br>open fun [getMessage](index.md#1068546184%2FFunctions%2F-82533025)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |
| [getStackTrace](index.md#-1238049138%2FFunctions%2F-82533025) | [jvm]<br>open fun [getStackTrace](index.md#-1238049138%2FFunctions%2F-82533025)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt; |
| [getSuppressed](index.md#1678506999%2FFunctions%2F-82533025) | [jvm]<br>fun [getSuppressed](index.md#1678506999%2FFunctions%2F-82533025)(): [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html)&gt; |
| [initCause](index.md#-104903378%2FFunctions%2F-82533025) | [jvm]<br>open fun [initCause](index.md#-104903378%2FFunctions%2F-82533025)(cause: [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html)): [Throwable](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html) |
| [printStackTrace](index.md#-1357294889%2FFunctions%2F-82533025) | [jvm]<br>open fun [printStackTrace](index.md#-1357294889%2FFunctions%2F-82533025)() |
| [setStackTrace](index.md#-1146009933%2FFunctions%2F-82533025) | [jvm]<br>open fun [setStackTrace](index.md#-1146009933%2FFunctions%2F-82533025)(stackTrace: [Array](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-array/index.html)&lt;[StackTraceElement](https://docs.oracle.com/javase/8/docs/api/java/lang/StackTraceElement.html)&gt;) |
| [toString](index.md#1869833549%2FFunctions%2F-82533025) | [jvm]<br>open fun [toString](index.md#1869833549%2FFunctions%2F-82533025)(): [String](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html) |

## Inheritors

| Name |
|---|
| [RIFFInvalidDataException](../-r-i-f-f-invalid-data-exception/index.md) |
| [InvalidFormatException](../-invalid-format-exception/index.md) |
