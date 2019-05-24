package com.dleibovych.splitthetrip.actions.transfer

import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.actions.TransactionAction
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
import org.junit.Before
import org.junit.Test

class AddTransferActionTest {

    private lateinit var messenger: TelegramMessenger
    private lateinit var storage: Storage
    private lateinit var action: TransactionAction

    @Before
    fun setUp() {
        messenger = mock()
        storage = mock()
        action = TransactionAction(storage)
    }

    @Test
    fun testNoSuchFromUserRegistered() {
        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, from = user, chat = createTelegramChat(1))
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            1,
            "Користувача не зареєстровано як платника. Ви можете зареєструватись як платник через /register"
        )
    }

    @Test
    fun testNothingProvided() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1)))

        val message = createTelegramMessage(1, text = "/transfer", chat = createTelegramChat(1), from = createTelegramUser(1, false, "first"))
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, "Щоб добавити витрату введіть суму після команди. /transfer _сума_")
    }

    @Test
    fun testNoValueProvided() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1)))


        val message = createTelegramMessage(1, text = "/transfer usd", chat = createTelegramChat(1), from = createTelegramUser(1, false, "first"))
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, "Щоб добавити витрату введіть суму після команди. /transfer _сума_")
    }

    @Test
    fun testValueProvidedSingleCurrency() {
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd")))
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1), BotUser(2, "second", 1)))

        val user = createTelegramUser(1, false, "first")
        val message = createTelegramMessage(1, text = "/transfer 16.01", chat = createTelegramChat(1), from = user)
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            1, "Оберіть людину якій ви переводите кошти.", replyMarkup = InlineKeyboardMarkup(
                listOf(
                    listOf(
                        InlineKeyboardButton("second", callbackData = "/confirmtransfer 1 2 16.01 usd")
                    )
                )
            )
        )
    }

    @Test
    fun testValueProvidedMultipleCurrency() {
        whenever(storage.readUsers()).thenReturn(listOf(BotUser(1, "first", 1), BotUser(2, "second", 1)))
        whenever(storage.getCurrencies()).thenReturn(listOf(Currency("usd"), Currency("uah")))

        val user = createTelegramUser(1, false, "first")
        val message = createTelegramMessage(1, text = "/transfer 16.01", chat = createTelegramChat(1), from = user)
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(
            1,
            "Оберіть валюту переказу.",
            replyMarkup = InlineKeyboardMarkup(
                listOf(
                    listOf(
                        InlineKeyboardButton("usd", callbackData = "/confirmtransfer 1 0 16.01 usd")
                    ),
                    listOf(
                        InlineKeyboardButton("uah", callbackData = "/confirmtransfer 1 0 16.01 uah")
                    )
                )
            )
        )
    }
}