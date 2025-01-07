package com.finalproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finalproject.utils.Utils

@Entity(tableName = "favcoins")
class FavCoin(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val cryptoCurrencyId: Int
)