package com.dleibovych.splitthetrip

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ExtensionTest {

    @Test
    fun testFirstInt() {
        val firstInt = "text 1".findFirstLong()

        assertEquals(1L, firstInt)
    }

    @Test
    fun testNoFirstInt() {
        val noFirstInt = "asdf".findFirstLong()

        assertNull(noFirstInt)
    }

    @Test
    fun testFindSecondInt() {
        val secondInt = "1 aasdf 2".findSecondLong()

        assertEquals(2L, secondInt)
    }

    @Test
    fun testFindNoSecondInt() {
        val secondInt = "1 aasdf".findSecondLong()

        assertNull(secondInt)
    }

    @Test
    fun testFindFirstDouble() {
        val firstDouble = "11.4".findFirstDouble()

        assertEquals(11.4, firstDouble)
    }

    @Test
    fun testFindFirstDoubleNoDecimal() {
        val firstDouble = "15".findFirstDouble()

        assertEquals(15.0, firstDouble)
    }

    @Test
    fun testFindSecondDouble() {
        val secondDouble = "1 11.4".findSecondDouble()

        assertEquals(11.4, secondDouble)
    }

    @Test
    fun testFindSecondDoubleComma() {
        val secondDouble = "1 11,4".findSecondDouble()

        assertEquals(11.4, secondDouble)
    }


    @Test
    fun testNoFirstDouble() {
        val firstDouble = "only text".findFirstDouble()

        assertNull(firstDouble)
    }

    @Test
    fun testFindLastText() {
        val firstText = "/action text".findLastNonActionText()

        assertEquals("text", firstText)
    }

    @Test
    fun testNoLastText() {
        val firstText = "/action".findLastNonActionText()

        assertNull(firstText)
    }

    @Test
    fun testMoneyToLong() {
        val result = 15.0.convertToMoneyLong()
        assertEquals(1500, result)
    }

    @Test
    fun testPreciseMoneyToLong() {
        val result = 15.001.convertToMoneyLong()
        assertEquals(1500, result)
    }

    @Test
    fun testFindFirstAction() {
        val result = "/action action".findAction()
        assertEquals("/action", result)
    }

    @Test
    fun testFindNoAction() {
        val result = "action".findAction()
        assertNull(result)
    }
}