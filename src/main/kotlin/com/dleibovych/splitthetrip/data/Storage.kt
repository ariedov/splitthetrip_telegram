package com.dleibovych.splitthetrip.data

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection

interface Storage {

    fun saveUser(user: BotUser)

    fun addCurrency(currency: Currency)

    fun getCurrencies(): List<Currency>

    fun readUsers(): List<BotUser>

    fun addExpense(expense: Expense)

    fun addTransfer(transfer: Transfer)

    fun clear()
}

class MongoStorage(private val database: MongoDatabase) : Storage {
    override fun saveUser(user: BotUser) {
        val collection = database.getCollection<BotUser>()
        collection.insertOne(user)
    }

    override fun addCurrency(currency: Currency) {
        val collection = database.getCollection<Currency>()
        collection.insertOne(currency)
    }

    override fun getCurrencies(): List<Currency> {
        val collection = database.getCollection<Currency>()
        return collection.find().toList()
    }

    override fun readUsers(): List<BotUser> {
        val collection = database.getCollection<BotUser>()
        return collection.find().toList()
    }

    override fun addExpense(expense: Expense) {
        val collection = database.getCollection<Expense>()
        collection.insertOne(expense)
    }

    override fun addTransfer(transfer: Transfer) {
        val collection = database.getCollection<Transfer>()
        collection.insertOne(transfer)
    }

    override fun clear() {
        database.drop()
    }
}