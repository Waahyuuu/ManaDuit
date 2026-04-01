package com.example.manaduit.ui

import android.app.Application
import androidx.lifecycle.*
import com.example.manaduit.data.*
import kotlinx.coroutines.launch

class TransactionViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: TransactionRepository

    val allData: LiveData<List<Transaction>>
    val totalIncome: LiveData<Long>
    val totalExpense: LiveData<Long>

    init {
        val dao = AppDatabase.getDatabase(application).transactionDao()
        repo = TransactionRepository(dao)

        allData = repo.allData
        totalIncome = repo.totalIncome
        totalExpense = repo.totalExpense
    }

    fun insert(transaction: Transaction) = viewModelScope.launch {
        repo.insert(transaction)
    }

    fun delete(transaction: Transaction) = viewModelScope.launch {
        repo.delete(transaction)
    }
}