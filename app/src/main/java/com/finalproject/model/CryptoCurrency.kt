package com.finalproject.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.finalproject.utils.Utils
import kotlinx.parcelize.Parcelize

@Parcelize
class CryptoCurrency (
    val id: Int,
    val name: String,
    val symbol: String,
    val slug: String,
    val quote: Quote
): Parcelable
{
    override fun toString(): String {
        return "CryptoCurrency(id=$id, name='$name', symbol='$symbol', slug='$slug', quote=$quote)"
    }
}
