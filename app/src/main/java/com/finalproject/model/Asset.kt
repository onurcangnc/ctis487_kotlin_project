package com.finalproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "assets")
class Asset(
    @PrimaryKey(autoGenerate = true)
    val id: Int =0,
    val cryptoCurrencyId: Int,
    var amount: Double

)