package com.finalproject.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finalproject.MainActivity
import com.finalproject.db.CryptoCurrencyDB
import com.finalproject.db.WalletDAO


@Entity(tableName = "wallet")
class Wallet(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var amount: Double = 500.0,
) {
    //default constructor
    constructor() : this(0, 500.0)
}