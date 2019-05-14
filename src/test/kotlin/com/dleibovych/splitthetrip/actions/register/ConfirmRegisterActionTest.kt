package com.dleibovych.splitthetrip.actions.register

import com.dleibovych.splitthetrip.*
import com.dleibovych.splitthetrip.actions.ConfirmRegisterAction
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.data.Storage
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ConfirmRegisterActionTest {

    private lateinit var storage: Storage
    private lateinit var messenger: TelegramMessenger
    private lateinit var action: ConfirmRegisterAction

    @Before
    fun setUp() {
        storage = mock()
        messenger = mock()
        action = ConfirmRegisterAction(storage)
    }

    @Test
    fun testIncorrectUserId() {
        val messageText =
            "/confirmregister 3-userid 3-responsiblefor" // it should get the numbers, the text doesn't really matter
        val user = createTelegramUser(
            id = 2,
            isBot = false,
            firstName = "name"
        ) // the userid is different from the one in message
        val message = createTelegramMessage(1, chat = createTelegramChat(1), from = user, text = messageText)

        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            chatId = eq(1),
            text = eq("Не вдалося зберегти платника."),
            replyMarkup = eq(null)
        )
    }

    @Test
    fun testExistingUser() {
        Assert.assertTrue(false)
    }

    @Test
    fun testSuccessfullySavedUser() {
        val messageText =
            "/confirmregister 3-userid 3-responsiblefor" // it should get the numbers, the text doesn't really matter
        val user = createTelegramUser(
            id = 3,
            isBot = false,
            firstName = "name"
        ) // the userid is different from the one in message
        val message = createTelegramMessage(1, chat = createTelegramChat(1), from = user, text = messageText)

        action.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            chatId = eq(1),
            text = eq("Успішно зберегли name як платника!"),
            replyMarkup = eq(null)
        )
    }
}