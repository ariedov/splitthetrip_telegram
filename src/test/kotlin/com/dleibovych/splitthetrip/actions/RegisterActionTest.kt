package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.createTelegramUpdate
import com.nhaarman.mockitokotlin2.mock
import me.ivmg.telegram.Bot
import org.junit.Before
import org.junit.Test

class RegisterActionTest {

    private lateinit var registerAction: RegisterAction

    @Before
    fun setUp() {
        registerAction = RegisterAction()
    }

    @Test
    fun testNullMessage() {
        val bot = mock<Bot>()
        registerAction.perform(bot, createTelegramUpdate(1))
    }
}