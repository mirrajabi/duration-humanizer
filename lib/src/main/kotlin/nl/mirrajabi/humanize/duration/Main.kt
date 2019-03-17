package nl.mirrajabi.humanize.duration

import nl.mirrajabi.humanize.duration.languages.LanguageDictionary

fun main(args: Array<String>) {
    val humanizer = DurationHumanizer()
    var options: DurationHumanizer.Options

    val ms = DurationHumanizer.TimeUnit.MILLISECOND.milliseconds
    val s = DurationHumanizer.TimeUnit.SECOND.milliseconds
    val m = DurationHumanizer.TimeUnit.MINUTE.milliseconds
    val h = DurationHumanizer.TimeUnit.HOUR.milliseconds
    val d = DurationHumanizer.TimeUnit.DAY.milliseconds
    val w = DurationHumanizer.TimeUnit.WEEK.milliseconds
    val mo = DurationHumanizer.TimeUnit.MONTH.milliseconds
    val y = DurationHumanizer.TimeUnit.YEAR.milliseconds

    println("Units:")
    println(humanizer.humanize(ms))
    println(humanizer.humanize(s))
    println(humanizer.humanize(m))
    println(humanizer.humanize(h))
    println(humanizer.humanize(d))
    println(humanizer.humanize(w))
    println(humanizer.humanize(mo))
    println(humanizer.humanize(y))

    println()
    println(humanizer.humanize(s + ms))
    println(humanizer.humanize(m + s))
    println(humanizer.humanize(h + m))
    println(humanizer.humanize(d + h))
    println(humanizer.humanize(w + d))
    println(humanizer.humanize(mo + w))
    println(humanizer.humanize(y + mo))

    println()
    println(humanizer.humanize(s + ms * 2))
    println(humanizer.humanize(m + s * 2))
    println(humanizer.humanize(h + m * 2))
    println(humanizer.humanize(d + h * 2))
    println(humanizer.humanize(w + d * 2))
    println(humanizer.humanize(mo + w * 2))
    println(humanizer.humanize(y + mo * 2))

    println()
    options = DurationHumanizer.Options(conjunction = " and ", serialComma = false, round = true)
    println(humanizer.humanize(m * 4 + s * 3 + ms * 2, options))
    println(humanizer.humanize(h * 4 + m * 3 + s * 2, options))
    println(humanizer.humanize(d * 4 + h * 3 + m * 2, options))
    println(humanizer.humanize(w * 3 + d * 3 + h * 2, options))
    println(humanizer.humanize(mo * 4 + w * 3 + d * 2, options))
    println(humanizer.humanize(y * 4 + mo * 3 + w * 2, options))
    println(humanizer.humanize(1600, options))


    // Basic Usage
    println()
    println(humanizer.humanize(3000))
    println(humanizer.humanize(2250))
    println(humanizer.humanize(97320000))
    println(humanizer.humanize(12000))

    // Providing custom language provider and fallback languages:
    // you can implement your own `LanguageDictionary` and put it in the languages map.
    // at this time only English is already implemented in this lib
    val customLanguage = object : LanguageDictionary {
        override fun provide(key: String, count: Double): String {
            return "$key-abc"
        }
    }
    val languages = mapOf("cl" to customLanguage)
    options = DurationHumanizer.Options(language = "bad language name", languages = languages, fallbacks = listOf("cl"))
    println(humanizer.humanize(97320000, options))

    // Delimiter
    options = DurationHumanizer.Options(delimiter = " - ")
    println(humanizer.humanize(97320000, options))

    // Spacer
    options = DurationHumanizer.Options(spacer = " whole ")
    println(humanizer.humanize(97320000, options))

    // Largest
    options = DurationHumanizer.Options(largest = 1)
    println(humanizer.humanize(97320000, options))

    // Units
    val units = listOf(DurationHumanizer.TimeUnit.HOUR, DurationHumanizer.TimeUnit.MINUTE)
    options = DurationHumanizer.Options(units = units)
    println(humanizer.humanize(97320000, options)) // 27 hours, 2 minutes
    // You can also have your own custom units
    val customUnit = DurationHumanizer.TimeUnit("o", 5000)
    options = DurationHumanizer.Options(
        units = listOf(customUnit),
        language = "cl",
        languages = mapOf("cl" to customLanguage)
    )
    println(humanizer.humanize(97320000, options)) // 19464 o-abc

    // Round
    options = DurationHumanizer.Options(units = listOf(DurationHumanizer.TimeUnit.HOUR), round = true)
    println(humanizer.humanize(97320000, options))

    // Decimal
    options = DurationHumanizer.Options(units = listOf(DurationHumanizer.TimeUnit.HOUR), round = false, decimal = ",")
    println(humanizer.humanize(97560000, options))

    // Conjunction
    options = DurationHumanizer.Options(conjunction = " and ")
    println(humanizer.humanize(22141000, options))
    options = DurationHumanizer.Options(conjunction = " and ", serialComma = false)
    println(humanizer.humanize(22141000, options))
}
