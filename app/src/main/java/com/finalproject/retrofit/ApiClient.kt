package com.ctis487.workerjsondatabase.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//STEP1
object ApiClient {

    private var retrofit: Retrofit? = null

    fun getClient(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl("https://pro-api.coinmarketcap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}