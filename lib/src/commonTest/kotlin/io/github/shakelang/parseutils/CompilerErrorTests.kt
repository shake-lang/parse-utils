package io.github.shakelang.parseutils

import io.github.shakelang.parseutils.characters.position.Position
import io.github.shakelang.parseutils.characters.position.PositionMap
import io.github.shakelang.parseutils.characters.source.CharacterSource
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class CompilerErrorTests {

    @Test
    fun testCompilerError() {
        val source = CharacterSource.from(genLengthString(30), "<source>")
        val map = PositionMap(source, intArrayOf())
        val error = CompilerError(
            "message", "TestingError", "Some details",
            map.resolve(10), map.resolve(10)
        )
        assertEquals("<source>:1:11", error.marker.source)
        assertEquals("1  012345678901234567890123456789", error.marker.preview)
        assertEquals("             ^", error.marker.marker)
        assertEquals(
            "1  0123456789" + Formatting.INVERT + Formatting.FGColor.RED + "0" +
                    Formatting.RESET + "1234567890123456789", error.marker.colorPreview
        )
    }

    @Test
    fun testCompilerErrorOverflowAfter() {

        val source = CharacterSource.from(genLengthString(60), "<source>")
        val map = PositionMap(source, intArrayOf())
        val error = CompilerError(
            "message", "TestingError", "Some details",
            Position(map, 10, 11, 1),
            Position(map, 10, 11, 1)
        )

        assertEquals("<source>:1:11", error.marker.source)
        assertEquals("1  0123456789012345678901234567890...+28", error.marker.preview)
        assertEquals("             ^", error.marker.marker)
        assertEquals(
            "1  0123456789" + Formatting.INVERT + Formatting.FGColor.RED + "0" +
                    Formatting.RESET + "12345678901234567890...+28", error.marker.colorPreview
        )
    }

    @Test
    fun testCompilerErrorOverflowBefore() {
        val source = CharacterSource.from(genLengthString(60), "<source>")
        val map = PositionMap(source, intArrayOf())
        val error = CompilerError(
            "message", "TestingError", "Some details",
            map.resolve(39), map.resolve(39)
        )

        assertEquals("<source>:1:40", error.marker.source)
        assertEquals("1  +18...890123456789012345678901234567890123456789", error.marker.preview)
        assertEquals("                              ^", error.marker.marker)
        assertEquals(
            "1  +18...890123456789012345678" + Formatting.INVERT + Formatting.FGColor.RED + "9" +
                    Formatting.RESET + "01234567890123456789", error.marker.colorPreview
        )
    }

    @Test
    fun testCompilerErrorOverflow() {
        val source = CharacterSource.from(genLengthString(100), "<source>")
        val map = PositionMap(source, intArrayOf())
        val error = CompilerError(
            "message", "TestingError", "Some details",
            map.resolve(49), map.resolve(49)
        )

        assertEquals("<source>:1:50", error.marker.source)
        assertEquals("1  +28...890123456789012345678901234567890123456789...+29", error.marker.preview)
        assertEquals("                              ^", error.marker.marker)
        assertEquals(
            "1  +28...890123456789012345678" + Formatting.INVERT + Formatting.FGColor.RED + "9" +
                    Formatting.RESET + "01234567890123456789...+29", error.marker.colorPreview
        )
    }

    @Test
    fun testCompilerErrorLongMark() {
        val source = CharacterSource.from(genLengthString(40), "<source>")
        val map = PositionMap(source, intArrayOf())
        val error = CompilerError(
            "message", "TestingError", "Some details",
            map.resolve(9),
            map.resolve(14)
        )

        assertEquals("<source>:1:10", error.marker.source)
        assertEquals("1  012345678901234567890123456789012345678", error.marker.preview)
        assertEquals("            ^^^^^^", error.marker.marker)
        assertEquals(
            "1  012345678" + Formatting.INVERT + Formatting.FGColor.RED + "901234" +
                    Formatting.RESET + "567890123456789012345678", error.marker.colorPreview
        )
    }

    @Test
    fun testCompilerErrorMultiLine() {
        val str = """
            
            ${genLengthString(38)}
            ${genLengthString(40)}
            ${genLengthString(39)}
            
            """.trimIndent()
        val source = CharacterSource.from(str, "<source>")
        val map = PositionMap(source, intArrayOf(0, 39, 80, 120))
        val pos = map.resolve(50)
        assertSame(50, pos.index)
        assertSame(11, pos.column)
        assertSame(3, pos.line)
        val error = CompilerError(
            "message", "TestingError", "Some details", pos, pos
        )

        assertEquals("<source>:3:11", error.marker.source)
        assertEquals("3  012345678901234567890123456789012...+7", error.marker.preview)
        assertEquals("             ^", error.marker.marker)
        assertEquals(
            "3  0123456789" + Formatting.INVERT + Formatting.FGColor.RED + "0" +
                    Formatting.RESET + "1234567890123456789012...+7", error.marker.colorPreview
        )

    }

    companion object {
        private fun genLengthString(length: Int): String {
            val string = StringBuilder()
            for (i in 0 until length) string.append(i % 10)
            return string.toString()
        }
    }
}