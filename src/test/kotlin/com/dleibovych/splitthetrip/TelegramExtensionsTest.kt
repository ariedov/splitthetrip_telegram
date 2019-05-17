package com.dleibovych.splitthetrip

import org.junit.Assert.assertEquals
import org.junit.Test

class TelegramExtensionsTest {

    @Test
    fun readCallbackQueryChatId() {
        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(1, callbackQuery = createCallbackQuery("1", user, data = "data", message = message))

        assertEquals(10L, update.chatId)
    }

    @Test
    fun readMessageChatId() {
        val message = createTelegramMessage(1, chat = createTelegramChat(10), text = "message")
        val update = createTelegramUpdate(1, message = message)

        assertEquals(10L, update.chatId)

    }

    @Test
    fun readCallbackQueryText() {
        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(1, callbackQuery = createCallbackQuery("1", user, data = "data", message = message))

        assertEquals("data", update.text)
    }

    @Test
    fun readMessageText() {
        val message = createTelegramMessage(1, chat = createTelegramChat(10), text = "message")
        val update = createTelegramUpdate(1, message = message)

        assertEquals("message", update.text)
    }

    @Test
    fun readCallbackQueryFrom() {
        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10))
        val update = createTelegramUpdate(1, callbackQuery = createCallbackQuery("1", user, data = "data", message = message))

        assertEquals(user, update.from)
    }

    @Test
    fun readMessageFrom() {
        val user = createTelegramUser(1, false, "name")
        val message = createTelegramMessage(1, chat = createTelegramChat(10), text = "message", from = user)
        val update = createTelegramUpdate(1, message = message)

        assertEquals(user, update.from)
    }
}