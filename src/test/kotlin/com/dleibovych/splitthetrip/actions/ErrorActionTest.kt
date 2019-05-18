package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class ErrorActionTest {

    private lateinit var messenger: TelegramMessenger
    private lateinit var action: ErrorAction

    @Before
    fun setUp() {
        messenger = mock()
        action = ErrorAction()
    }

    @Test
    fun testErrorAction() {
        action.perform(messenger, createTelegramUpdate(1, message = createTelegramMessage(1, chat = createTelegramChat(1))))

        verify(messenger).sendMessage(1, "Сталася помилка.")
    }
}