package com.dleibovych.splitthetrip

val numberRegex = "\\.*(\\d+)".toRegex()

fun String.findFirstLong() : Long? {
    return numberRegex.find(this)?.groupValues?.get(1)?.toLong()
}

fun String.findSecondLong() : Long? {
    val allMatches = numberRegex.findAll(this).toList()
    return if (allMatches.size > 1) {
        allMatches[1].groupValues[0].toLong()
    }
    else {
        null
    }
}

fun String.findFirstNonActionText(): String? {
    val textRegex = "/.+ (.+)".toRegex()
    return textRegex.find(this)?.groupValues?.get(1)
}