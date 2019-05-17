package com.dleibovych.splitthetrip.actions.expense

import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.dleibovych.splitthetrip.createTelegramUser
import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Expense
import com.dleibovych.splitthetrip.data.Storage
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test

class ConfirmExpenseActionTest {

    private lateinit var storage: Storage
    private lateinit var messenger: TelegramMessenger
    private lateinit var action: ConfirmExpenseAction

    @Before
    fun setUp() {
        storage = mock()
        messenger = mock()

        action = ConfirmExpenseAction(storage)
    }

    @Test
    fun testWrongUserAndCallerId() {
        val message = createTelegramMessage(
            1,
            text = "/confirmadd 1 15.77 usd",
            chat = createTelegramChat(1),
            from = createTelegramUser(2, isBot = false, firstName = "name")
        )
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, text = "Підтвердити операцію має той хто її почав.")
    }

    @Test
    fun testNoSuchPayer() {
        whenever(storage.readUsers()).thenReturn(emptyList())

        val message = createTelegramMessage(
            1,
            text = "/confirmadd 1 15.77 usd",
            chat = createTelegramChat(1),
            from = createTelegramUser(1, isBot = false, firstName = "name")
        )
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, text = "name не зареєстровано як платника. Ви можете зареєструватись як платник виконавши /register")
    }

    @Test
    fun testNoSuchCurrency() {
        whenever(storage.getCurrencies()).thenReturn(emptyList())
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "name", 3)))

        val message = createTelegramMessage(
            1,
            text = "/confirmadd 1 15.77 usd",
            chat = createTelegramChat(1),
            from = createTelegramUser(1, isBot = false, firstName = "name")
        )
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, text = "Валюта usd не добавлена. Ви можете добавити її через /addcurrency")
    }

    @Test
    fun testMultipleDigitsAfterComma() {
        val user = BotUser(1, "name", 4)
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.readUsers()).thenReturn(listOf(user))

        val message = createTelegramMessage(
            1,
            text = "/confirmadd 1 15.7123 usd",
            chat = createTelegramChat(1),
            from = createTelegramUser(1, isBot = false, firstName = "name")
        )
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(storage).addExpense(Expense(user, 1571, Currency("usd")))
        verify(messenger).sendMessage(1, text = "Операцію збережено")
    }

    @Test
    fun testOneDigitAfterComma() {
        val user = BotUser(1, "name", 4)
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.readUsers()).thenReturn(listOf(user))

        val message = createTelegramMessage(
            1,
            text = "/confirmadd 1 15.7 usd",
            chat = createTelegramChat(1),
            from = createTelegramUser(1, isBot = false, firstName = "name")
        )
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(storage).addExpense(Expense(user, 1570, Currency("usd")))
        verify(messenger).sendMessage(1, text = "Операцію збережено")
    }

    @Test
    fun testSuccessfullySaveDecimalExpense() {
        val user = BotUser(1, "name", 4)
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.readUsers()).thenReturn(listOf(user))

        val message = createTelegramMessage(
            1,
            text = "/confirmadd 1 15.77 usd",
            chat = createTelegramChat(1),
            from = createTelegramUser(1, isBot = false, firstName = "name")
        )
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(storage).addExpense(Expense(user, 1577, Currency("usd")))
        verify(messenger).sendMessage(1, text = "Операцію збережено")
    }

    @Test
    fun testSuccessfullySaveWholeExpense() {
        val user = BotUser(1, "name", 4)
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.readUsers()).thenReturn(listOf(user))

        val message = createTelegramMessage(
            1,
            text = "/confirmadd 1 15 usd",
            chat = createTelegramChat(1),
            from = createTelegramUser(1, isBot = false, firstName = "name")
        )
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(storage).addExpense(Expense(user, 1500, Currency("usd")))
        verify(messenger).sendMessage(1, text = "Операцію збережено")
    }
}