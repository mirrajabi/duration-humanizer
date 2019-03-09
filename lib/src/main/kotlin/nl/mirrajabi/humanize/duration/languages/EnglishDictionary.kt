package nl.mirrajabi.humanize.duration.languages

class EnglishDictionary : LanguageDictionary {
    private val words = mapOf(
        DictionaryKeys.YEAR to "year",
        DictionaryKeys.MONTH to "month",
        DictionaryKeys.WEEK to "week",
        DictionaryKeys.DAY to "day",
        DictionaryKeys.HOUR to "hour",
        DictionaryKeys.MINUTE to "minute",
        DictionaryKeys.SECOND to "second",
        DictionaryKeys.MILLISECOND to "millisecond",
        DictionaryKeys.DECIMAL to "."
    )

    override fun provide(key: String, count: Double): String {
        return words[key] + (if (count != 1.0 && key != DictionaryKeys.DECIMAL) "s" else "")
    }
}