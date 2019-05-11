package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.findFirstLong
import com.dleibovych.splitthetrip.findSecondLong
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
}