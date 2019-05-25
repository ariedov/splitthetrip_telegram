package com.dleibovych.splitthetrip.data

import java.time.Instant
import java.util.*

data class Expense(
    val user: BotUser,
    val amount: Long,
    val currency: Currency,
    val description: String? = null
//    val date: Date = Date.from(
//        Instant.now()
//    )
)

data class Transfer(
    val from: BotUser,
    val to: BotUser,
    val amount: Long,
    val currency: Currency
//    val date: Date = Date.from(Instant.now())
)