package nl.mirrajabi.humanize.duration.languages

import nl.mirrajabi.humanize.duration.DurationHumanizer
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for [ItalianDictionary].
 */
class ItalianDictionaryTests {

    private val options = DurationHumanizer.Options(
        language = "it",
        languages = mapOf("it" to ItalianDictionary())
    )
    private val humanizer = DurationHumanizer()

    @Test fun `README examples are properly humanized`() {
        assertEquals("3 secondi", humanizer.humanize(3000, options))
        assertEquals("2 secondi, 250 millisecondi", humanizer.humanize(2250, options))
        assertEquals("1 giorno, 3 ore, 2 minuti", humanizer.humanize(97320000, options))
    }
}