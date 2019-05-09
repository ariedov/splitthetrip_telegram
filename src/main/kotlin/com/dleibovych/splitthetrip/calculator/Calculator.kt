package com.dleibovych.splitthetrip.calculator

fun calculateShare(users: List<User>, totalPeople: Int) : List<Share> {
    val totalBalance = users.fold(0L) { result, user -> result + user.balance }
    return users.map {user ->
        val owns = user.responsibleFor / totalPeople.toDouble() * totalBalance
        Share(user, user.balance - owns.toLong())
    }
}

data class Share(val user: User, val share: Long)