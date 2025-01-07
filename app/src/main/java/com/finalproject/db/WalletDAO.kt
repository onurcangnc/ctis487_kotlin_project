package com.finalproject.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.finalproject.model.CryptoCurrency
import com.finalproject.model.Wallet

@Dao
interface WalletDAO {
    @Query("SELECT * FROM wallet ORDER BY id")
    fun getWallet(): List<Wallet>

    @Insert
    fun insertWallet(wallet: Wallet)

    @Update
    fun updateWallet(wallet: Wallet)

    @Query("DELETE FROM wallet")
    fun deleteAll()

}