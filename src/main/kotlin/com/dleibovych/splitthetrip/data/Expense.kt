package com.dleibovych.splitthetrip.data

import java.util.*

data class Expense(val user: BotUser, val amount: Long, val currency: String, val description: String, val date: Date)

data class Transfer(val from: BotUser, val to: BotUser, val amount: Long, val currency: String, val date: Date)