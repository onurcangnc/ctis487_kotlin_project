package com.finalproject.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finalproject.model.CryptoCurrency
import com.finalproject.utils.Utils

//@Dao
//interface CryptoCurrencyDAO {
//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    fun insertCryptoCurrency(cryptoCurrency: CryptoCurrency)
//    @Update
//    fun updateCryptoCurrency(cryptoCurrency: CryptoCurrency)
//    @Delete
//    fun deleteCryptoCurrency(cryptoCurrency: CryptoCurrency)
//    @Query("SELECT * FROM ${Utils.TABLENAME} ORDER BY id DESC")
//    fun getAllCryptoCurrency(): List<CryptoCurrency>
//}