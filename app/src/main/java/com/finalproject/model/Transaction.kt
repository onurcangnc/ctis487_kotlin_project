package com.finalproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import java.util.UUID
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Entity(tableName = "transactions")
class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val coinId: Int,
    val transactionType: TransactionType,
    val amount: Double,
    val pricePerCoin: Double,
    val date: LocalDateTime = LocalDateTime.now()
) {
    private fun calculateTotalValue(): Double {
        return amount * pricePerCoin
    }

    override fun toString(): String {
        return "Transaction(id=$id, coinId=$coinId, transactionType=$transactionType, amount=$amount, pricePerCoin=$pricePerCoin, date=$date)"
    }
}

