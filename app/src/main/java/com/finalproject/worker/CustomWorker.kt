package com.finalproject.worker

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ctis487.workerjsondatabase.retrofit.ApiClient
import com.ctis487.workerjsondatabase.retrofit.ApiService
import com.finalproject.model.CryptoCurrency
import com.finalproject.system.CoinSystem

class CustomWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {
    override fun doWork(): Result {
        val service = ApiClient.getClient().create(ApiService::class.java)
        val response = service.getCryptoCurrencies().execute()

        if (response.isSuccessful) {
            response.body()?.let { cryptoResponse ->
                CoinSystem.cryptoList.clear()
                CoinSystem.cryptoList.addAll(cryptoResponse.data) // Access the `data` field
//                CoinSystem.cryptoList =  CoinSystem.cryptoList
                return Result.success()
            }
        } else {
            Log.d("CustomWorker", "Failed to fetch data:")
        }

        return Result.failure()
    }
}

