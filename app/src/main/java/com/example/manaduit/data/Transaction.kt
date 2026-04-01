package com.example.manaduit.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(

    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val transactionId: String,
    val type: String, // income / expense

    val title: String,
    val description: String?,

    val amount: Long,
    val date: Long
)