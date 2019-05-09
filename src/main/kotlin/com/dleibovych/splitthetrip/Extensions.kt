package com.dleibovych.splitthetrip

val numberRegex = "\\.*(\\d+)".toRegex()

fun String.findFirstInt() : Int? {
    return numberRegex.find(this)?.groupValues?.get(1)?.toInt()
}

fun String.findSecondInt() : Int? {
    val allMatches = numberRegex.findAll(this).toList()
    return if (allMatches.size > 1) {
        allMatches[1].groupValues[0].toInt()
    }
    else {
        null
    }
}