package com.dleibovych.splitthetrip.actions.expense

import com.dleibovych.splitthetrip.actions.Action
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.Update

class ConfirmExpenseAction(private val storage: Storage) : Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {

    }
}