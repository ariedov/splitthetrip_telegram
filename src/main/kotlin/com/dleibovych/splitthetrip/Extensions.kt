package com.dleibovych.splitthetrip

val numberRegex = "(\\d+)".toRegex()

fun String.findFirstLong(): Long? {
    return numberRegex.find(this)?.groupValues?.get(1)?.toLong()
}

fun String.findSecondLong(): Long? {
    val allMatches = numberRegex.findAll(this).toList()
    return if (allMatches.size > 1) {
        allMatches[1].groupValues[0].toLong()
    } else {
        null
    }
}

fun String.findFirstDouble(): Double? {
    val doubleRegex = "(\\d+\\.*\\d*)".toRegex()
    return doubleRegex.find(this)?.groupValues?.get(1)?.toDouble()
}

fun String.findSecondDouble(): Double? {
    val doubleRegex = "(\\d+[.|,]*\\d*)".toRegex()
    val allMatches = doubleRegex.findAll(this).toList()
    return if (allMatches.size > 1) {
        allMatches[1].groupValues[0].replace(",", ".").toDouble()
    } else {
        null
    }
}

fun String.findLastNonActionText(): String? {
    val textRegex = "/.+ (.+)".toRegex()
    return textRegex.find(this)?.groupValues?.last()
}

fun Double.convertToMoneyLong(): Long {
    return String.format("%.2f", this).replace(".", "").toLong()
}

fun String.findAction(): String? {
    val actionRegex = "(/\\w+)".toRegex()
    return actionRegex.find(this)?.groupValues?.first()
}

fun String.chunksCount(): Int {
    return split(" ").size
}

fun String.getNumbersCount(): Int {
    val chunks = split(" ")
    return chunks.sumBy { if (it.findFirstDouble() == null) 0 else 1 }
}

