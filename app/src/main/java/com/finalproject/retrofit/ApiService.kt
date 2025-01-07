package com.ctis487.workerjsondatabase.retrofit


import com.finalproject.model.CryptoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers


interface ApiService {

    @Headers("X-CMC_PRO_API_KEY: 535f9379-c0c9-46e4-b82d-638aecca4897") // API Key in the header
    @GET("v1/cryptocurrency/listings/latest")
    fun getCryptoCurrencies(): Call<CryptoResponse>
}