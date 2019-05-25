package com.dleibovych.splitthetrip.actions.transfer

import com.dleibovych.splitthetrip.*
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.actions.TransactionChecker
import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Storage
import com.dleibovych.splitthetrip.data.Transfer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test

class ConfirmTransferTest {

    private lateinit var storage: Storage
    private lateinit var messenger: TelegramMessenger
    private lateinit var action: ConfirmTransactionAction

    @Before
    fun setUp() {
        storage = mock()
        messenger = mock()
        action = ConfirmTransactionAction(storage, TransactionChecker())
    }

    @Test
    fun testConfirmIdDifferentFromId() {
        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(
            1,
            callbackQuery = createCallbackQuery("1", user, data = "/confirmtransfer 2 1 15.02 usd", message = message)
        )

        action.perform(messenger, update)

        verify(messenger).sendMessage(10, "Підтвердити операцію має той хто її почав.")
    }

    @Test
    fun testInvalidFromUser() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1)))

        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(
            1,
            callbackQuery = createCallbackQuery("1", user, data = "/confirmtransfer 2 1 15.02 usd", message = message)
        )

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            10,
            "Відправника не зареєстровано як платника. Ти можеш зареєструвати себе через /register"
        )
    }

    @Test
    fun testInvalidToUser() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1)))

        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(
            1,
            callbackQuery = createCallbackQuery("1", user, data = "/confirmtransfer 1 2 15.02 usd", message = message)
        )

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            10,
            "Отримувача не зареєстровано як платника. Ти можеш зареєструвати себе через /register"
        )
    }

    @Test
    fun testInvalidCurrency() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1), BotUser(2, "second", 1)))

        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(
            1,
            callbackQuery = createCallbackQuery("1", user, data = "/confirmtransfer 1 2 15.02 usd", message = message)
        )

        action.perform(messenger, update)

        verify(messenger).sendMessage(10, "Валюта usd не зареєстровано. Ти можеш додати валюту через /addcurrency _валюта_")
    }

    @Test
    fun testNoTo() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1), BotUser(2, "second", 1)))
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))

        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(
            1,
            callbackQuery = createCallbackQuery("1", user, data = "/confirmtransfer 1 15.02 usd", message = message)
        )

        action.perform(messenger, update)

        verify(messenger).sendMessage(10, "Сталася помилка, недостатньо інформації.")
    }

    @Test
    fun testNoUsers() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1), BotUser(2, "second", 1)))
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))

        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(
            1,
            callbackQuery = createCallbackQuery("1", user, data = "/confirmtransfer 15.02 usd", message = message)
        )

        action.perform(messenger, update)

        verify(messenger).sendMessage(10, "Сталася помилка, недостатньо інформації.")
    }


    @Test
    fun testSuccessfullySavedTransfer() {
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1), BotUser(2, "second", 1)))

        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(
            1,
            callbackQuery = createCallbackQuery("1", user, data = "/confirmtransfer 1 2 15.02 usd", message = message)
        )

        action.perform(messenger, update)

        verify(messenger).sendMessage(10, "Переказ успішно збережено.")
        verify(storage).addTransfer(
            Transfer(
                BotUser(1, "first", 1), BotUser(2, "second", 1),
                1502, Currency("usd")
            )
        )
    }
}