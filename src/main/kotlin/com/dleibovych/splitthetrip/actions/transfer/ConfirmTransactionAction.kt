package com.dleibovych.splitthetrip.actions.transfer

import com.dleibovych.splitthetrip.actions.Action
import com.dleibovych.splitthetrip.actions.TelegramMessenger
import com.dleibovych.splitthetrip.actions.TransactionChecker
import com.dleibovych.splitthetrip.data.Storage
import me.ivmg.telegram.entities.Update

class ConfirmTransactionAction(private val storage: Storage,
                               private val checker: TransactionChecker): Action {

    override fun perform(messenger: TelegramMessenger, update: Update) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}