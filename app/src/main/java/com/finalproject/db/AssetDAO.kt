package com.finalproject.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.finalproject.model.Asset
import com.finalproject.model.CryptoCurrency
import com.finalproject.utils.Utils
import retrofit2.http.GET

@Dao
interface AssetDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAsset(asset: Asset)
    @Query("SELECT * FROM assets ORDER BY id DESC")
    fun getAllAsset(): List<Asset>

    @Query("SELECT * FROM assets WHERE cryptoCurrencyId = :cryptoCurrencyId")
    fun getAssetByCryptoCurrencyId(cryptoCurrencyId: Int): Asset

    @Query("SELECT * FROM assets WHERE id = :id")
    fun getAsset(id: Int): Asset
    @Update
    fun updateAsset(asset: Asset)
    @Delete
    fun deleteAsset(asset: Asset)
}