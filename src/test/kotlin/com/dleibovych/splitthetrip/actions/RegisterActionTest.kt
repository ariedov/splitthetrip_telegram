package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.createTelegramChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.dleibovych.splitthetrip.createTelegramUser
import com.nhaarman.mockitokotlin2.*
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.MessageEntity
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
            chatId = eq(1),
            text = any(),
            replyMarkup = eq(null)
        )
    }

    @Test
    fun testSendCorrectCallbackFromUser() {
        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, createTelegramChat(1), from = user, text = "4")
        registerAction.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            chatId = eq(1),
            text = any(),
            replyMarkup = eq(
                InlineKeyboardButton(
                    "Підтвердити!",
                    callbackData = "/confirmregister 1 4"
                )
            )
        )
    }

    @Test
    fun testSendCorrectCallbackMentionedUser() {
        val user = createTelegramUser(3, false, "name")
        val message = createTelegramMessage(
            1,
            createTelegramChat(1),
            text = "3",
            entities = listOf(MessageEntity("mention", offset = 0, length = 10, user = user, url = null))
        )
        registerAction.perform(messenger, createTelegramUpdate(1, message = message))

        verify(messenger).sendMessage(
            chatId = eq(1),
            text = any(),
            replyMarkup = eq(
                InlineKeyboardButton(
                    "Підтвердити!",
                    callbackData = "/confirmregister 3 3"
                )
            )
        )
    }
}