Duration Humanizer
=================
[![](https://jitpack.io/v/mirrajabi/duration-humanizer.svg)](https://jitpack.io/#mirrajabi/duration-humanizer)

Port of [HumanizeDuration.js](https://github.com/EvanHahn/HumanizeDuration.js).  
I have the time in milliseconds and I want it to become "30 minutes" or "3 days, 1 hour". Enter Duration Humanizer!

Adding to your project
-----------
1. Add the JitPack repository to your root build.gradle file

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

2. Add the dependency
```
dependencies {
	implementation "com.github.mirrajabi:duration-humanizer:$latest_version"
}
```
**Note**. Replace `latest_version` with the latest humanizer version published: [![]

Basic usage
-----------
```kotlin
val humanizer = DurationHumanizer()
humanizer.humanize(12000) // "12 seconds"
```

Usage
-----

By default, Duration Humanizer will humanize down to the second, and will return a decimal for the smallest unit. It will humanize in English by default.

```kotlin
humanizer.humanize(3000)      // "3 seconds"
humanizer.humanize(2250)      // "2.25 seconds"
humanizer.humanize(97320000)  // "1 day, 3 hours, 2 minutes"
```

### Options

You can change the settings by passing options as the second argument:

**language and fallbacks**

Language for unit display (accepts an [ISO 639-1 code](https://en.wikipedia.org/wiki/List_of_ISO_639-1_codes) from one of the [supported languages](#supported-languages)).  
Fallback languages if the provided language cannot be found (accepts an ISO 639-1 code from one of the supported languages). It works from left to right.

__Custom language__  
You can implement your own `LanguageDictionary` and put it in the languages map.

```kotlin
val customLanguage = object : LanguageDictionary {
    override fun provide(key: String, count: Double): String {
        return "${key}-unit"
    }
}
val languages = mapOf("cl" to customLanguage)
val options = DurationHumanizer.Options(language = "bad language name", languages = languages, fallbacks = listOf("cl"))
println(humanizer.humanize(97320000, options))
```

**delimiter**

String to display between the previous unit and the next value.

```kotlin
val options = DurationHumanizer.Options(delimiter = " - ")
println(humanizer.humanize(97320000, options)) // 1 day - 3 hours - 2 minutes
```

**spacer**

String to display between each value and unit.

```kotlin
val options = DurationHumanizer.Options(spacer = " whole ")
println(humanizer.humanize(97320000, options)) // 1 whole day, 3 whole hours, 2 whole minutes
```

**largest**

Number representing the maximum number of units to display for the duration.

```kotlin
val options = DurationHumanizer.Options(largest = 1)
println(humanizer.humanize(97320000)) // 1 day, 3 hours, 2 minutes
println(humanizer.humanize(97320000, options)) // 1 day
```

**units**

List of `DurationHumanizer.TimeUnit`s to define which units are used to display the duration (if needed). Can be one, or a combination of any, of the following:
`TimeUnit.YEAR`, `TimeUnit.MONTH`, `TimeUnit.WEEK`, `TimeUnit.DAY`, `TimeUnit.HOUR`, `TimeUnit.MINUTE`, `TimeUnit.SECOND`, `TimeUnit.MILLISECOND`

```kotlin
val units = listOf(DurationHumanizer.TimeUnit.HOUR, DurationHumanizer.TimeUnit.MINUTE)
var options = DurationHumanizer.Options(units = units)
println(humanizer.humanize(97320000, options)) // 27 hours, 2 minutes
// You can also have your own custom units
val customUnit = DurationHumanizer.TimeUnit("o", 5000)
options = DurationHumanizer.Options(
    units = listOf(customUnit),
    language = "cl",
    languages = mapOf("cl" to customLanguage)
)
println(humanizer.humanize(97320000, options)) // 19464 o-abc
```

**round**

Boolean value. Use `true` to [round](https://en.wikipedia.org/wiki/Rounding#Round_half_up) the smallest unit displayed (can be combined with `largest` and `units`).

```kotlin
val options = DurationHumanizer.Options(units = listOf(DurationHumanizer.TimeUnit.HOUR), round = true)
println(humanizer.humanize(97320000, options)) // 27 hours
```

**decimal**

String to substitute for the decimal point in a decimal fraction.

```kotlin
val options = DurationHumanizer.Options(units = listOf(DurationHumanizer.TimeUnit.HOUR), round = false, decimal = ",")
println(humanizer.humanize(97560000, options)) // 27,1 hours
```

**conjunction**

String to include before the final unit. You can also set `serialComma` to `false` to eliminate the final comma.

```kotlin
var options = DurationHumanizer.Options(conjunction = " and ")
println(humanizer.humanize(22141000, options)) // 6 hours, 9 minutes, and 1 second
options = DurationHumanizer.Options(conjunction = " and ", serialComma = false)
println(humanizer.humanize(22141000, options)) // 6 hours, 9 minutes and 1 second
```

Supported languages
-------------------

Duration Humanizer by default has only English support.  
If you want add more languages, you can include the following dependencies.

**Note**. Replace `latest_version` with the latest humanizer version published: [![](https://jitpack.io/v/mirrajabi/duration-humanizer.svg)](https://jitpack.io/#mirrajabi/duration-humanizer)  

* Italian  
  `implementation com.github.mirrajabi:duration-humanizer-it:latest_version`
