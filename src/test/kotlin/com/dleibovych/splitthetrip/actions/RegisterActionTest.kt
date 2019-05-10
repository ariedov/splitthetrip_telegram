package com.dleibovych.splitthetrip.actions

import com.nhaarman.mockitokotlin2.mock
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.Update
import org.junit.Before
import org.junit.Test

class RegisterActionTest {

    lateinit var registerAction: RegisterAction

    @Before
    fun setUp() {
        registerAction = RegisterAction()
    }

    @Test
    fun testNullMessage() {
        val bot = mock<Bot>()
        registerAction.perform(bot, Update(0, null, null, null, null, null, null, null))
    }
}