package com.example.manaduit.data

class TransactionRepository(private val dao: TransactionDao) {

    val allData = dao.getAll()
    val totalIncome = dao.getTotalIncome()
    val totalExpense = dao.getTotalExpense()

    suspend fun insert(transaction: Transaction) {
        dao.insert(transaction)
    }

    suspend fun delete(transaction: Transaction) {
        dao.delete(transaction)
    }
}