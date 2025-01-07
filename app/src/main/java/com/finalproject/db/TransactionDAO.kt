package com.finalproject.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finalproject.utils.Utils
import com.finalproject.model.Transaction

@Dao
interface TransactionDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTransaction(transaction: Transaction)
    @Update
    fun updateTransaction(transaction: Transaction)
    @Delete
    fun deleteTransaction(transaction: Transaction)
    @Query("SELECT * FROM transactions ORDER BY id DESC")
    fun getAllTransaction(): List<Transaction>
}