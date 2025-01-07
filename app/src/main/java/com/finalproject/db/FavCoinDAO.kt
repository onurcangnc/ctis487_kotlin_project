package com.finalproject.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.finalproject.model.FavCoin

@Dao
interface FavCoinDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavCoin(favCoin: FavCoin)
    @Query("SELECT cryptoCurrencyId FROM favcoins ORDER BY id DESC")
    fun getAllFavCoin(): List<Int>
}