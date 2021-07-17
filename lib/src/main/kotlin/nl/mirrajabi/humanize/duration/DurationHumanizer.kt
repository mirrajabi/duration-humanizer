package nl.mirrajabi.humanize.duration

import nl.mirrajabi.humanize.duration.languages.DictionaryKeys
import nl.mirrajabi.humanize.duration.languages.EnglishDictionary
import nl.mirrajabi.humanize.duration.languages.LanguageDictionary
import java.lang.Math.floor

class DurationHumanizer {
    private val languages = mapOf("en" to EnglishDictionary())

    @JvmOverloads
    fun humanize(milliseconds: Long, options: Options = Options()): String {
        val dictionary = getDictionary(options)
        val units = options.units.sortedByDescending { it.milliseconds }
        val pieces = getPieces(milliseconds, units)

        if (options.round) {
            round(pieces, options)
        }

        val result = calculateResult(units, pieces, options, dictionary)

        if (result.isNotEmpty()) {
            if (options.conjunction.isEmpty() || result.size == 1) {
                return result.joinToString(separator = options.delimiter)
            } else if (result.size == 2) {
                return result.joinToString(separator = options.conjunction)
            } else if (result.size > 2) {
                val joinedPiecesExceptTheLastOne = result.subList(0, result.size - 1)
                    .joinToString(separator = options.delimiter)
                val lastPiece = result.lastOrNull().orEmpty()
                val optionalComma = if (options.serialComma) "," else ""
                return joinedPiecesExceptTheLastOne + optionalComma + options.conjunction + lastPiece
            }
        }

        return render(0.0, units.last(), dictionary, options)
    }

    private fun getPieces(milliseconds: Long, units: List<TimeUnit>): MutableList<Pair<TimeUnit, Double>> {
        var ms = Math.abs(milliseconds).toDouble()
        val pieces = mutableListOf<Pair<TimeUnit, Double>>()
        for (i in 0 until units.size) {
            val unit = units[i]

            val unitCount = if (i + 1 == units.size) {
                ms / unit.milliseconds
            } else {
                floor(ms / unit.milliseconds.toDouble())
            }

            pieces.add(unit to unitCount)

            ms -= unitCount * unit.milliseconds
        }
        return pieces
    }

    private fun round(pieces: MutableList<Pair<TimeUnit, Double>>, options: Options) {
        val firstOccupiedUnitIndex = pieces.indexOfFirst { it.second > 0 }
        for (i in pieces.size - 1 downTo 0) {
            val count = Math.round(pieces[i].second).toDouble()
            pieces[i] = pieces[i].copy(second = count)
            val piece = pieces[i]

            if (i == 0) {
                break
            }

            val previousPiece = pieces[i - 1].first

            val ratioToLargerUnit = previousPiece.milliseconds / piece.first.milliseconds.toDouble()
            val l = options.largest
            if ((count % ratioToLargerUnit) == 0.0 || (l != null && (l.minus(1) < (i - firstOccupiedUnitIndex)))) {
                pieces[i - 1] = pieces[i - 1].first to (pieces[i - 1].second + count / ratioToLargerUnit)
                pieces[i] = piece.first to 0.0
            }
        }
    }

    private fun calculateResult(
        units: List<TimeUnit>,
        pieces: MutableList<Pair<TimeUnit, Double>>,
        options: Options,
        dictionary: LanguageDictionary
    ): List<String> {
        val result = mutableListOf<String>()
        for (i in 0 until units.size) {
            val piece = pieces[i]
            if (piece.second > 0) {
                result.add(render(piece.second, piece.first, dictionary, options))
            }

            if (result.size.toLong() == options.largest) {
                break
            }
        }
        return result
    }

    private fun render(count: Double, type: TimeUnit, dictionary: LanguageDictionary, options: Options): String {
        val decimal = if (options.decimal.isNullOrEmpty()) {
            dictionary.provide(DictionaryKeys.DECIMAL, 0.0)
        } else {
            options.decimal
        }
        var stringCount = count.toString()
        val hasDecimals = !("$stringCount ").contains(".0 ")
        stringCount = if (hasDecimals) {
            stringCount.replace(".", decimal.orEmpty())
        } else {
            stringCount.replace(".0", "")
        }
        val word = dictionary.provide(type.key, count)
        return stringCount + options.spacer + word
    }

    private fun getDictionary(options: Options): LanguageDictionary {
        val languagesFromOptions = mutableListOf<String>()
        languagesFromOptions.add(options.language)

        val fallbacks = options.fallbacks
        if (fallbacks != null) {
            if (fallbacks.isNotEmpty()) {
                languagesFromOptions.addAll(fallbacks)
            } else {
                throw IllegalArgumentException("fallbacks must be an array with at least one element")
            }
        }

        for (i in 0 until languagesFromOptions.size) {
            val languageToTry = languagesFromOptions[i]
            val optionLanguages = options.languages
            if (optionLanguages?.get(languageToTry) != null) {
                return optionLanguages.getValue(languageToTry)
            } else if (languages[languageToTry] != null) {
                return languages.getValue(languageToTry)
            }
        }

        throw IllegalStateException("No language found")
    }

    data class Options @JvmOverloads constructor(
        var language: String = "en",
        var delimiter: String = ", ",
        var spacer: String = " ",
        var conjunction: String = "",
        var serialComma: Boolean = true,
        var round: Boolean = false,
        var largest: Long? = null,
        var decimal: String? = null,
        var languages: Map<String, LanguageDictionary>? = null,
        var fallbacks: List<String>? = null,
        var units: List<TimeUnit> = listOf(
            TimeUnit.YEAR,
            TimeUnit.MONTH,
            TimeUnit.WEEK,
            TimeUnit.DAY,
            TimeUnit.HOUR,
            TimeUnit.MINUTE,
            TimeUnit.SECOND,
            TimeUnit.MILLISECOND
        )
    )

    data class TimeUnit constructor(val key: String, val milliseconds: Long) {
        companion object {
            @JvmStatic
            val YEAR = TimeUnit(DictionaryKeys.YEAR, 31536000000)
            @JvmStatic
            val MONTH = TimeUnit(DictionaryKeys.MONTH, 2592000000)
            @JvmStatic
            val WEEK = TimeUnit(DictionaryKeys.WEEK, 604800000)
            @JvmStatic
            val DAY = TimeUnit(DictionaryKeys.DAY, 86400000)
            @JvmStatic
            val HOUR = TimeUnit(DictionaryKeys.HOUR, 3600000)
            @JvmStatic
            val MINUTE = TimeUnit(DictionaryKeys.MINUTE, 60000)
            @JvmStatic
            val SECOND = TimeUnit(DictionaryKeys.SECOND, 1000)
            @JvmStatic
            val MILLISECOND = TimeUnit(DictionaryKeys.MILLISECOND, 1)
        }
    }
}