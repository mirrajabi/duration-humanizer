package nl.mirrajabi.humanize.duration

import nl.mirrajabi.humanize.duration.languages.EnglishDictionary
import nl.mirrajabi.humanize.duration.languages.KeyConstants
import nl.mirrajabi.humanize.duration.languages.LanguageDictionary
import java.lang.Math.floor

class DurationHumanizer {

    private val languages = mapOf("en" to EnglishDictionary())
    fun humanize(milliseconds: Long, options: Options = Options()): String {
        var ms = Math.abs(milliseconds).toDouble()

        val dictionary = getDictionary(options)
        val pieces = mutableListOf<Pair<TimeUnit, Double>>()
        val units = options.units.sortedByDescending { it.milliseconds }
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

        val firstOccupiedUnitIndex = pieces.indexOfFirst { it.second > 0 }

        if (options.round) {
            for (i in pieces.size - 1 downTo 0) {
                val count = Math.round(pieces[i].second).toDouble()
                pieces[i] = pieces[i].copy(second = count)
                val piece = pieces[i]

                if (i == 0) {
                    break
                }

                val previousPiece = pieces[i - 1].first

                val ratioToLargerUnit = previousPiece.milliseconds / piece.first.milliseconds.toDouble()
                if ((count % ratioToLargerUnit) == 0.0 || (options.largest != null && (options.largest.minus(1) < (i - firstOccupiedUnitIndex)))) {
                    pieces[i - 1] = pieces[i - 1].first to (pieces[i - 1].second + count / ratioToLargerUnit)
                    pieces[i] = piece.first to 0.0
                }
            }
        }

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

        if (result.isNotEmpty()) {
            if (options.conjunction.isEmpty() || result.size == 1) {
                return result.joinToString(separator = options.delimiter)
            } else if (result.size == 2) {
                return result.joinToString(separator = options.conjunction)
            } else if (result.size > 2) {
                return result.subList(0, result.size - 1)
                    .joinToString(separator = options.delimiter) +
                        (if (options.serialComma) "," else " ") +
                        options.conjunction +
                        result.lastOrNull().orEmpty()
            }
        }

        return render(0.0, units.last(), dictionary, options)
    }

    private fun render(count: Double, type: TimeUnit, dictionary: LanguageDictionary, options: Options): String {
        val decimal =
            if (options.decimal.isNullOrEmpty()) dictionary.get(KeyConstants.DECIMAL, 0.0) else options.decimal
        val countStr = count.toString().replace(".", decimal)
        val word = dictionary.get(type.key, count)
        return countStr + options.spacer + word
    }

    private fun getDictionary(options: Options): LanguageDictionary {
        val languagesFromOptions = mutableListOf<String>()
        languagesFromOptions.add(options.language)

        if (options.fallbacks != null) {
            if (options.fallbacks.isNotEmpty()) {
                languagesFromOptions.addAll(options.fallbacks)
            } else {
                throw IllegalArgumentException("fallbacks must be an array with at least one element")
            }
        }

        for (i in 0 until languagesFromOptions.size) {
            val languageToTry = languagesFromOptions[i]
            if (options.languages?.getOrDefault(languageToTry, null) != null) {
                return options.languages.getValue(languageToTry)
            } else if (languages.getOrDefault(languageToTry, null) != null) {
                return languages.getValue(languageToTry)
            }
        }

        throw IllegalStateException("No language found")
    }

    data class Options(
        val language: String = "en",
        val delimiter: String = ", ",
        val spacer: String = " ",
        val conjunction: String = "",
        val serialComma: Boolean = true,
        val round: Boolean = false,
        val largest: Long? = null,
        val decimal: String? = null,
        val languages: Map<String, LanguageDictionary>? = null,
        val fallbacks: List<String>? = null,
        val units: List<TimeUnit> = listOf(
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
            val YEAR = TimeUnit(KeyConstants.YEAR, 31536000000)
            @JvmStatic
            val MONTH = TimeUnit(KeyConstants.MONTH, 2592000000)
            @JvmStatic
            val WEEK = TimeUnit(KeyConstants.WEEK, 604800000)
            @JvmStatic
            val DAY = TimeUnit(KeyConstants.DAY, 86400000)
            @JvmStatic
            val HOUR = TimeUnit(KeyConstants.HOUR, 3600000)
            @JvmStatic
            val MINUTE = TimeUnit(KeyConstants.MINUTE, 60000)
            @JvmStatic
            val SECOND = TimeUnit(KeyConstants.SECOND, 1000)
            @JvmStatic
            val MILLISECOND = TimeUnit(KeyConstants.MILLISECOND, 1)
        }
    }
}