package com.dleibovych.splitthetrip.actions.expense

import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.dleibovych.splitthetrip.createTelegramUser
import com.dleibovych.splitthetrip.data.Storage
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
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

        verify(messenger).sendMessage(1, text = "Підтвердити операцію має той самий користувач що її почав.")
    }

    @Test
    fun testSuccessfullySaveExpense() {
        val message = createTelegramMessage(
            1,
            text = "/confirmadd 1 15.77 usd",
            chat = createTelegramChat(1),
            from = createTelegramUser(1, isBot = false, firstName = "name")
        )
        val update = createTelegramUpdate(1, message = message)

        action.perform(messenger, update)

        verify(messenger).sendMessage(1, text = "Операцію збережено")
    }
}