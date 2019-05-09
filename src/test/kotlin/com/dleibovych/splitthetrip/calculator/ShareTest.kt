package com.dleibovych.splitthetrip.calculator

import com.dleibovych.splitthetrip.data.User
import com.dleibovych.splitthetrip.calculator.calculateShare
import org.junit.Assert.*
import org.junit.Test

class ShareTest {

    @Test
    fun testCalculationOneToOne() {
        val share = calculateShare(
            listOf(
                User(1, "first", 1, 1000),
                User(2, "second", 1, 0)
            )
        )

        assertEquals(2, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(500, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-500, share[1].share)
    }

    @Test
    fun testCalculationOneToOneWithThree() {
        val share = calculateShare(
            listOf(
                User(1, "first", 1, 900),
                User(2, "second", 1, 0),
                User(3, "third", 1, 0)
            )
        )

        assertEquals(3, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(600, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-300, share[1].share)

        assertEquals(3, share[2].user.id)
        assertEquals(-300, share[2].share)
    }

    @Test
    fun testCalculateShareWithTwoPayers() {
        val share = calculateShare(
            listOf(
                User(1, "first", 3, 10000),
                User(2, "second", 4, 4000)
            )
        )

        assertEquals(2, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(4000, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-4000, share[1].share)
    }

    @Test
    fun testCalculateShareWithThreePayers() {
        val share = calculateShare(
            listOf(
                User(1, "first", 3, 1000),
                User(2, "second", 7, 5000),
                User(3, "third", 3, 7000)
            )
        )

        assertEquals(3, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(-2000, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-2000, share[1].share)

        assertEquals(3, share[2].user.id)
        assertEquals(4000, share[2].share)
    }
}