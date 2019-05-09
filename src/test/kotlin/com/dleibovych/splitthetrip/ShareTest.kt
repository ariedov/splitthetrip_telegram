package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.calculator.User
import com.dleibovych.splitthetrip.calculator.calculateShare
import org.junit.Assert.*
import org.junit.Test

class ShareTest {

    @Test
    fun testCalculateShareWithTwoPayers() {
        val share = calculateShare(
            listOf(
                User(1, "first", 3, 10000),
                User(2, "second", 4, 4000)
            ),
            7
        )

        assertEquals(2, share.size)

        assertEquals(1, share[0].user.id)
        assertEquals(4000, share[0].share)

        assertEquals(2, share[1].user.id)
        assertEquals(-4000, share[1].share)
    }
}