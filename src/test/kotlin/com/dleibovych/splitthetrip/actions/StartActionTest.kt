package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.dleibovych.splitthetrip.data.Storage
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test

class StartActionTest {

    private lateinit var action: StartAction
    private lateinit var storage: Storage
    private lateinit var messenger: TelegramMessenger

    @Before
    fun setUp() {
        messenger = mock()
        storage = mock()
        action = StartAction(storage)
    }

    @Test
    fun testStartNewTrip() {
        action.perform(messenger, createTelegramUpdate(1, message = createTelegramMessage(1, chat = createTelegramChat(1))))

        verify(messenger).sendMessage(eq(1), any(), eq(null))
        verify(storage).clear()
    }

}