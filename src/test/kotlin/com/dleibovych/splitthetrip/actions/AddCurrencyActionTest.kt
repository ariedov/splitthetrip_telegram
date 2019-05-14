package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AddCurrencyActionTest {

    private lateinit var messenger: TelegramMessenger
    private lateinit var action: AddCurrencyAction

    @Before
    fun setUp() {
        messenger = mock()
        action = AddCurrencyAction()
    }

    @Test
    fun testNoCurrency() {
        val message = createTelegramMessage(1, createTelegramChat(1), text = "/addcurrency")
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, text = "Вкажіть назву валюти у форматі: ${AddCurrencyAction.name} _назва_", replyMarkup = null)
    }

    @Test
    fun testInitiateCurrencySave() {
        Assert.assertTrue(false)
    }

    @Test
    fun testSuccessfullySaveCurrency() {
        Assert.assertTrue(false)
    }
}