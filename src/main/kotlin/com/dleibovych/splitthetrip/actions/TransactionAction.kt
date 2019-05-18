package com.dleibovych.splitthetrip.actions

import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.Update

class TransactionAction(private val storage: Storage): Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class ConfirmTransactionAction: Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}