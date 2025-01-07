package com.finalproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Quote(
    val USD: USD
) : Parcelable