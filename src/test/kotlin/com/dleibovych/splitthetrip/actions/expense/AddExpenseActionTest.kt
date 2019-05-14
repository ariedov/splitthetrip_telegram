package com.dleibovych.splitthetrip.actions.expense

import com.dleibovych.splitthetrip.actions.AddExpenseAction
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.dleibovych.splitthetrip.createTelegramUser
import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Storage
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.InlineKeyboardMarkup
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class AddExpenseActionTest {

    private lateinit var storage: Storage
    private lateinit var messenger: TelegramMessenger
    private lateinit var action: AddExpenseAction

    @Before
    fun setUp() {
        messenger = mock()
        storage = mock()
        action = AddExpenseAction(storage)
    }

    @Test
    fun testRequestExpenseNoCurrency() {
        whenever(storage.getCurrencies()).thenReturn(emptyList())

        val user = createTelegramUser(1, isBot = false, firstName = "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/add 145.15", from = user)
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            1,
            text = "Спочатку добавте валюту в якій можна вести підрахунки через /addcurrency",
            replyMarkup = null
        )
    }

    @Test
    fun testRequestExpenseOneCurrency() {
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("default")))

        val user = createTelegramUser(1, isBot = false, firstName = "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/add 145.15", from = user)
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            1, text = "Підтвердити платіж 145.15 default?", replyMarkup = InlineKeyboardButton(
                text = "Підтвердити!",
                callbackData = "/confirmadd 1 145.15 default"
            )
        )
    }

    @Test
    fun testRequestExpenseNoSuchCurrency() {
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("default")))

        val user = createTelegramUser(1, isBot = false, firstName = "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/add 145.15 usd", from = user)
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            1,
            text = "Немає валюти usd. Ви можете її додати через /addcurrency",
            replyMarkup = null
        )
    }

    @Test
    fun testRequestExpenseMultipleCurrencies() {
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("default"), Currency("usd")))

        val user = createTelegramUser(1, isBot = false, firstName = "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/add 145.15", from = user)
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            1, text = "Оберіть валюту яку хочете викорстати.", replyMarkup = InlineKeyboardMarkup(
                inlineKeyboard = listOf(
                    listOf(
                        InlineKeyboardButton(
                            text = "default",
                            callbackData = "/add 145.15 default"
                        ),
                        InlineKeyboardButton(
                            text = "usd",
                            callbackData = "/add 145.15 usd"
                        )
                    )
                )
            )
        )
    }

    @Test
    fun testNoSuchUserRegistered() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "name", 4)))

        val user = createTelegramUser(1, isBot = false, firstName = "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/add 2 145.15", from = user)
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, text = "name не зареєстровано як платника. Можете зареєструвати через /register")

        Assert.assertTrue(false)
    }
}