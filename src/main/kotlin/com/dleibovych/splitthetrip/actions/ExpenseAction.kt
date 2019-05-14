package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.Update

class AddExpenseAction(private val storage: Storage): Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {

    }

}