package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.createChat
import com.dleibovych.splitthetrip.createTelegramMessage
import com.dleibovych.splitthetrip.createTelegramUpdate
import com.nhaarman.mockitokotlin2.*
import me.ivmg.telegram.Bot
import me.ivmg.telegram.entities.InlineKeyboardButton
import me.ivmg.telegram.entities.MessageEntity
import me.ivmg.telegram.entities.User
import org.junit.Before
import org.junit.Test

class RegisterActionTest {

    private lateinit var registerAction: RegisterAction

    @Before
    fun setUp() {
        registerAction = RegisterAction()
    }

    @Test
    fun testSendCallbackWithNoUser() {
        val bot = mock<Bot>()

        val message = createTelegramMessage(
            1,
            createChat(1),
            text = "3"
        )
        registerAction.perform(bot, createTelegramUpdate(1, message = message))

        verify(bot).sendMessage(
            chatId = eq(1),
            text = anyOrNull(),
            parseMode = anyOrNull(),
            disableWebPagePreview = anyOrNull(),
            disableNotification = anyOrNull(),
            replyToMessageId = anyOrNull(),
            replyMarkup = eq(null)
        )
    }

    @Test
    fun testSendCorrectCallbackFromUser() {
        val bot = mock<Bot>()

        val user = User(1, false, "name", null, null, null)
        val message = createTelegramMessage(1, createChat(1), from = user, text = "4")
        registerAction.perform(bot, createTelegramUpdate(1, message = message))

        verify(bot).sendMessage(
            chatId = eq(1),
            text = anyOrNull(),
            parseMode = anyOrNull(),
            disableWebPagePreview = anyOrNull(),
            disableNotification = anyOrNull(),
            replyToMessageId = anyOrNull(),
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
        val bot = mock<Bot>()

        val user = User(3, false, "name", null, null, null)
        val message = createTelegramMessage(
            1,
            createChat(1),
            text = "3",
            entities = listOf(MessageEntity("mention", offset = 0, length = 10, user = user, url = null))
        )
        registerAction.perform(bot, createTelegramUpdate(1, message = message))

        verify(bot).sendMessage(
            chatId = eq(1),
            text = anyOrNull(),
            parseMode = anyOrNull(),
            disableWebPagePreview = anyOrNull(),
            disableNotification = anyOrNull(),
            replyToMessageId = anyOrNull(),
            replyMarkup = eq(
                InlineKeyboardButton(
                    "Підтвердити!",
                    callbackData = "/confirmregister 3 3"
                )
            )
        )
    }
}