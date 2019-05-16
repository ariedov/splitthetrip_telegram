package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.actions.*
import com.dleibovych.splitthetrip.actions.expense.AddExpenseAction
import com.dleibovych.splitthetrip.actions.expense.ConfirmExpenseAction
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ActionRouterTest {

    private lateinit var router: ActionRouter

    @Before
    fun setUp() {
        router = ActionRouter()
    }

    @Test
    fun testExpenseAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/add 15.0")
        val update = createTelegramUpdate(1, message)

        val result = router.createAction(update)

        assertTrue(result is AddExpenseAction)
    }

    @Test
    fun testConfirmExpenseAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "Any text")
        val update = createTelegramUpdate(
            1, message,
            callbackQuery = createCallbackQuery("1", createTelegramUser(1, false, "name"), "/confirmadd 123 123 usd")
        )

        val result = router.createAction(update)

        assertTrue(result is ConfirmExpenseAction)
    }

    @Test
    fun testCurrencyAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/addcurrency 15.0")
        val update = createTelegramUpdate(1, message)

        val result = router.createAction(update)

        assertTrue(result is AddCurrencyAction)
    }

    @Test
    fun testConfirmCurrencyAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "Any text")
        val update = createTelegramUpdate(
            1, message,
            callbackQuery = createCallbackQuery("1", createTelegramUser(1, false, "name"), "/confirmcurrency usd")
        )

        val result = router.createAction(update)

        assertTrue(result is ConfirmNewCurrencyAction)
    }

    @Test
    fun testInfoAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/info")
        val update = createTelegramUpdate(1, message)

        val result = router.createAction(update)

        assertTrue(result is InfoAction)
    }

    @Test
    fun testRegisterAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/register 15.0")
        val update = createTelegramUpdate(1, message)

        val result = router.createAction(update)

        assertTrue(result is RegisterAction)
    }

    @Test
    fun testConfirmRegisterAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "Any text")
        val update = createTelegramUpdate(
            1, message,
            callbackQuery = createCallbackQuery("1", createTelegramUser(1, false, "name"), "/confirmregister usd")
        )

        val result = router.createAction(update)

        assertTrue(result is ConfirmRegisterAction)
    }

    @Test
    fun testStartAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/start")
        val update = createTelegramUpdate(1, message)

        val result = router.createAction(update)

        assertTrue(result is StartAction)
    }

    @Test
    fun testTransactionAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "/transfer 1 2 12 usd")
        val update = createTelegramUpdate(1, message)

        val result = router.createAction(update)

        assertTrue(result is TransactionAction)
    }

    @Test
    fun testConfirmTransactionAction() {
        val message = createTelegramMessage(1, chat = createTelegramChat(1), text = "Any text")
        val update = createTelegramUpdate(
            1, message,
            callbackQuery = createCallbackQuery("1", createTelegramUser(1, false, "name"), "/confirmtransfer 1 2 12 usd")
        )

        val result = router.createAction(update)

        assertTrue(result is ConfirmTransactionAction)
    }
}