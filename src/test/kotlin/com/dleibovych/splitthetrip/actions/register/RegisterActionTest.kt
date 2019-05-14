package com.dleibovych.splitthetrip.actions.register

import com.dleibovych.splitthetrip.actions.RegisterAction
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.dleibovych.splitthetrip.createTelegramUser
import com.nhaarman.mockitokotlin2.*
import me.ivmg.telegram.entities.InlineKeyboardButton
import org.junit.Before
import org.junit.Test

class RegisterActionTest {

    private lateinit var registerAction: RegisterAction
    private lateinit var messenger: TelegramMessenger

    @Before
    fun setUp() {
        registerAction = RegisterAction()
        messenger = mock()
    }

    @Test
    fun testSendCallbackWithNoUser() {
        val message = createTelegramMessage(
            1,
            createTelegramChat(1),
            text = "3"
        )
        registerAction.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            chatId = 1,
            text = "Сталася помилка, не зрозуміло кого зберігати.",
            replyMarkup = null
        )
    }

    @Test
    fun testSendCorrectCallbackFromUser() {
        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, createTelegramChat(1), from = user, text = "4")
        registerAction.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            chatId = 1,
            text = "name готовий платити за 4.",
            replyMarkup =
                InlineKeyboardButton(
                    "Підтвердити!",
                    callbackData = "/confirmregister 1 4"
            )
        )
    }
}