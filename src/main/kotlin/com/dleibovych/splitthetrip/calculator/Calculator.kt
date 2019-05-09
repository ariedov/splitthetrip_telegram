package com.dleibovych.splitthetrip.calculator

import com.dleibovych.splitthetrip.data.BotUser
import kotlin.math.abs
import kotlin.math.min

fun calculateShare(users: List<BotUser>): List<Share> {
    val totalBalance = users.fold(0L) { result, user -> result + user.balance }
    val totalPeople = users.fold(0L) { result, user -> result + user.responsibleFor }
    return users.map { user ->
        val owns = user.responsibleFor / totalPeople.toDouble() * totalBalance
        Share(user, user.balance - owns.toLong())
    }
}

fun calculateDebt(user: BotUser, shares: List<Share>): List<Share> {
    val userShare = shares.find { it.user.id == user.id }

    // share is > 0, no debts
    if (userShare!!.share > 0) {
        return listOf()
    }

    return shares
        .filter { it != userShare }
        .filter { it.share > 0 }
        .map { Share(it.user, min(abs(userShare.share), it.share)) }
}

data class Share(val user: BotUser, val share: Long)