package com.dleibovych.splitthetrip.actions.currency

import com.dleibovych.splitthetrip.actions.AddCurrencyAction
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import me.ivmg.telegram.entities.InlineKeyboardButton
import org.junit.Before
import org.junit.Test

class AddCurrencyActionTest {

    private lateinit var messenger: TelegramMessenger
    private lateinit var initiateAction: AddCurrencyAction

    @Before
    fun setUp() {
        messenger = mock()
        initiateAction = AddCurrencyAction()
    }

    @Test
    fun testNoCurrency() {
        val message = createTelegramMessage(1, createTelegramChat(1), text = "/addcurrency")
        val update = createTelegramUpdate(1, message = message)

        initiateAction.perform(messenger, update)

        verify(messenger).sendMessage(
            1,
            text = "Вкажіть назву валюти у форматі: /addcurrency _назва_",
            replyMarkup = null
        )
    }

    @Test
    fun testInitiateCurrencySave() {
        val message = createTelegramMessage(1, createTelegramChat(1), text = "/addcurrency usd")
        val update = createTelegramUpdate(1, message = message)

        initiateAction.perform(messenger, update)

        verify(messenger).sendMessage(
            1, "Додати нову валюту *usd*?", replyMarkup = InlineKeyboardButton(
                text = "Так",
                callbackData = "/confirmcurrency usd"
            )
        )
    }
}