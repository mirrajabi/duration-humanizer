package nl.mirrajabi.humanize.duration.languages

class EnglishDictionary : LanguageDictionary {
    private val words = mapOf(
        KeyConstants.YEAR to "year",
        KeyConstants.MONTH to "month",
        KeyConstants.WEEK to "week",
        KeyConstants.DAY to "day",
        KeyConstants.HOUR to "hour",
        KeyConstants.MINUTE to "minute",
        KeyConstants.SECOND to "second",
        KeyConstants.MILLISECOND to "millisecond",
        KeyConstants.DECIMAL to "."
    )

    override fun get(key: String, count: Long): String {
        return words[key] + (if (count != 1L && key != KeyConstants.DECIMAL) "s" else "")
    }
}