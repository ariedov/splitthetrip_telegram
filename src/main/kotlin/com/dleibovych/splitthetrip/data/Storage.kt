package com.dleibovych.splitthetrip.data

import com.mongodb.client.MongoDatabase
import org.litote.kmongo.getCollection

class Storage(val database: MongoDatabase) {

    fun saveUser(user: BotUser) {
        val collection = database.getCollection<BotUser>()
        collection.insertOne(user)
    }

    fun addCurrency(currency: Currency) {
        val collection = database.getCollection<Currency>()
        collection.insertOne(currency)
    }

    fun getCurrencies(): List<Currency> {
        val collection = database.getCollection<Currency>()
        return collection.find().toList()
    }

    fun readUsers(): List<BotUser> {
        val collection = database.getCollection<BotUser>()
        return collection.find().toList()
    }

    fun addExpense(expense: Expense) {
        val collection = database.getCollection<Expense>()
        collection.insertOne(expense)
    }

    fun addTransfer(transfer: Transfer) {
        val collection = database.getCollection<Transfer>()
        collection.insertOne(transfer)
    }

    fun getExpenses(): List<Expense> {
        val collection = database.getCollection<Expense>()
        return collection.find().toList()
    }

    fun getTransfers(): List<Transfer> {
        val collection = database.getCollection<Transfer>()
        return collection.find().toList()
    }

    fun clear() {
        database.drop()
    }
}