package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.actions.*
import com.dleibovych.splitthetrip.actions.expense.AddExpenseAction
import com.dleibovych.splitthetrip.actions.expense.ConfirmExpenseAction
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.CallbackQuery
import me.ivmg.telegram.entities.Update

class ActionRouter(private val storage: Storage) {

    fun createAction(update: Update): Action {
        val callbackAction = update.callbackQuery?.data?.findAction()
        val messageAction = update.message?.text?.findAction()

        return when (callbackAction ?: messageAction) {
            "/start" -> StartAction(storage)
            "/register" -> RegisterAction()
            "/confirmregister" -> ConfirmRegisterAction(storage)
            "/addcurrency" -> AddCurrencyAction()
            "/confirmcurrency" -> ConfirmNewCurrencyAction(storage)
            "/add" -> AddExpenseAction(storage)
            "/confirmadd" -> ConfirmExpenseAction(storage)
            "/transfer" -> TransactionAction()
            "/confirmtransfer" -> ConfirmTransactionAction()
            "/info" -> InfoAction(storage)
            else -> ErrorAction()
        }
    }
}