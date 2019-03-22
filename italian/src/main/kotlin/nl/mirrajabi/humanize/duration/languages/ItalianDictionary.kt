package nl.mirrajabi.humanize.duration.languages

/**
 * Italian implementation of [LanguageDictionary].
 */
class ItalianDictionary : LanguageDictionary {
    private val words = mapOf(
        DictionaryKeys.YEAR to "anno",
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
        val isPlural = count != 1.0

        // Italian is strange :s we cannot infer a rule to consistently determine plural nouns
        // We need to go manual then
        return when {
            key == DictionaryKeys.YEAR && isPlural -> "anni"
            key == DictionaryKeys.YEAR -> "anno"
            key == DictionaryKeys.MONTH && isPlural -> "mesi"
            key == DictionaryKeys.MONTH -> "mese"
            key == DictionaryKeys.WEEK && isPlural -> "settimane"
            key == DictionaryKeys.WEEK -> "settimana"
            key == DictionaryKeys.DAY && isPlural -> "giorni"
            key == DictionaryKeys.DAY -> "giorno"
            key == DictionaryKeys.HOUR && isPlural -> "ore"
            key == DictionaryKeys.HOUR -> "ora"
            key == DictionaryKeys.MINUTE && isPlural -> "minuti"
            key == DictionaryKeys.MINUTE -> "minuto"
            key == DictionaryKeys.SECOND && isPlural -> "secondi"
            key == DictionaryKeys.SECOND -> "secondo"
            key == DictionaryKeys.MILLISECOND && isPlural -> "millisecondi"
            key == DictionaryKeys.MILLISECOND -> "millisecondo"
            key == DictionaryKeys.DECIMAL -> ","
            else -> throw UnsupportedOperationException("Invalid key $key")
        }
    }
}