package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.dleibovych.splitthetrip.data.BotUser
import com.dleibovych.splitthetrip.data.Currency
import com.dleibovych.splitthetrip.data.Expense
import com.dleibovych.splitthetrip.data.Storage
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import org.junit.Before
import org.junit.Test

class InfoBuilderTest {

    private lateinit var messenger: TelegramMessenger
    private lateinit var storage: Storage
    private lateinit var action: InfoAction

    @Before
    fun setUp() {
        messenger = mock()
        storage = mock()

        action = InfoAction(storage)
    }

    @Test
    fun testBuildTotalInfo() {
        val user1 = BotUser(1, "user1", 1)
        val user2 = BotUser(2, "user2", 1)
        val user3 = BotUser(3, "user3", 1)

        whenever(storage.readUsers()).thenReturn(listOf(user1, user2, user3))
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.getExpenses()).thenReturn(listOf(Expense(user1, 900, Currency("usd"))))
        whenever(storage.getTransfers()).thenReturn(emptyList())

        val message = createTelegramMessage(1, chat = createTelegramChat(1))
        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            1,
            text = "user2 має повернути user1 - *300 usd*\nuser3 має повернути user1 - *300 usd*"
        )
    }

    @Test
    fun testBuildNoUsers() {
        whenever(storage.readUsers()).thenReturn(emptyList())
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.getExpenses()).thenReturn(emptyList())
        whenever(storage.getTransfers()).thenReturn(emptyList())

        val message = createTelegramMessage(1, chat = createTelegramChat(1))
        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(1, text = "Немає зареєстрованих людей. Ви можете добавити себе через /register")
    }

    @Test
    fun testBuildNoExpenses() {
        val user1 = BotUser(1, "user1", 1)
        val user2 = BotUser(2, "user2", 1)

        whenever(storage.readUsers()).thenReturn(listOf(user1, user2))
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.getExpenses()).thenReturn(emptyList())
        whenever(storage.getTransfers()).thenReturn(emptyList())

        val message = createTelegramMessage(1, chat = createTelegramChat(1))
        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            1,
            text = "Немає зареєстрованих витрат. Ви можете добавити одну через /add _сума_"
        )
    }

    @Test
    fun testBuildSingleUserOneCurrency() {
        val user1 = BotUser(1, "user1", 1)

        whenever(storage.readUsers()).thenReturn(listOf(user1))
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.getExpenses()).thenReturn(listOf(Expense(user1, 900, Currency("usd"))))
        whenever(storage.getTransfers()).thenReturn(emptyList())

        val message = createTelegramMessage(1, chat = createTelegramChat(1))
        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(1, text = "user1 витратив *900 usd*")
    }

    @Test
    fun testBuildSingleUserMultipleCurrencies() {
        val user1 = BotUser(1, "user1", 1)

        whenever(storage.readUsers()).thenReturn(listOf(user1))
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd"), Currency("uah")))
        whenever(storage.getExpenses()).thenReturn(
            listOf(
                Expense(user1, 900, Currency("usd")),
                Expense(user1, 100, Currency("uah"))
            )
        )
        whenever(storage.getTransfers()).thenReturn(emptyList())

        val message = createTelegramMessage(1, chat = createTelegramChat(1))
        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(1, text = "user1 витратив *900 usd*\nuser1 витратив *100 uah*")
    }


    @Test
    fun testBuildNoCurrencies() {
        val user1 = BotUser(1, "user1", 1)

        whenever(storage.readUsers()).thenReturn(listOf(user1))
        whenever(storage.getCurrencies()).thenReturn(emptyList())
        whenever(storage.getExpenses()).thenReturn(emptyList())
        whenever(storage.getTransfers()).thenReturn(emptyList())

        val message = createTelegramMessage(1, chat = createTelegramChat(1))
        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            1,
            text = "Немає жодної зареєстрованої валюти. Ви можете добавити одну через /addcurrency _назва_"
        )
    }

    @Test
    fun testDecimals() {
        val user1 = BotUser(1, "user1", 1)

        whenever(storage.readUsers()).thenReturn(listOf(user1))
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.getExpenses()).thenReturn(listOf(Expense(user1, 1570, Currency("usd"))))
        whenever(storage.getTransfers()).thenReturn(emptyList())

        val message = createTelegramMessage(1, chat = createTelegramChat(1))
        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(1, text = "user1 витратив *15.7 usd*")
    }
}