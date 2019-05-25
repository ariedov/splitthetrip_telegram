package com.dleibovych.splitthetrip

import com.dleibovych.splitthetrip.actions.*
import com.dleibovych.splitthetrip.actions.expense.AddExpenseAction
import com.dleibovych.splitthetrip.actions.expense.ConfirmExpenseAction
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.CallbackQuery
import me.ivmg.telegram.entities.Update
import java.util.logging.Logger

class ActionRouter(private val storage: Storage) {

    fun createAction(update: Update): Action = when (update.text?.findAction()) {
        "/start" -> StartAction(storage)
        "/register" -> RegisterAction()
        "/confirmregister" -> ConfirmRegisterAction(storage)
        "/addcurrency" -> AddCurrencyAction()
        "/confirmcurrency" -> ConfirmNewCurrencyAction(storage)
        "/add" -> AddExpenseAction(storage)
        "/confirmadd" -> ConfirmExpenseAction(storage)
        "/transfer" -> TransactionAction(storage, TransactionChecker())
        "/confirmtransfer" -> ConfirmTransactionAction()
        "/info" -> InfoAction(storage)
        else -> ErrorAction()
    }
}
