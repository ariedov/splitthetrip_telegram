package com.dleibovych.splitthetrip.actions.currency

import com.dleibovych.splitthetrip.actions.ConfirmNewCurrencyAction
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Storage
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test

class ConfirmCurrencyActionTest {

    private lateinit var storage: Storage
    private lateinit var messenger: TelegramMessenger
    private lateinit var action: ConfirmNewCurrencyAction

    @Before
    fun setUp() {
        storage = mock()
        messenger = mock()

        action = ConfirmNewCurrencyAction(storage)
    }

    @Test
    fun testSuccessfullySaveCurrency() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/confirmcurrency usd")
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(storage).addCurrency(Currency("usd"))
        verify(messenger).sendMessage(1, "Успіщно збережено валюту usd", replyMarkup = null)
    }

    @Test
    fun testExistingCurrency() {
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/confirmcurrency usd")
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(storage, never()).addCurrency(any())
        verify(messenger).sendMessage(1, "Валюта usd вже збережена.", replyMarkup = null)
    }

    @Test
    fun testNoCurrency() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/confirmcurrency")
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, "Вкажіть назву валюти у форматі: /addcurrency _назва_", replyMarkup = null)
    }
}