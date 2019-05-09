package com.dleibovych.splitthetrip.calculator

import kotlin.math.abs
import kotlin.math.min

fun calculateShare(users: List<User>, totalPeople: Int): List<Share> {
    val totalBalance = users.fold(0L) { result, user -> result + user.balance }
    return users.map { user ->
        val owns = user.responsibleFor / totalPeople.toDouble() * totalBalance
        Share(user, user.balance - owns.toLong())
    }
}

fun calculateDebt(user: User, shares: List<Share>): List<Share> {
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

data class Share(val user: User, val share: Long)