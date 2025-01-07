package com.finalproject.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class USD(
    val price: Double,
    val volume_24h: Double,
    val market_cap: Double,
    val percent_change_24h: Double
): Parcelable
