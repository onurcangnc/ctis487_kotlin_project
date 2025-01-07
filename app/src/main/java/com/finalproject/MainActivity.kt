package com.finalproject



import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.ctis487.workerjsondatabase.retrofit.ApiClient
import com.ctis487.workerjsondatabase.retrofit.ApiService
import com.example.cryptotradingapp.adapter.CryptoRecyclerViewAdapter
import com.finalproject.db.CryptoCurrencyDB
import com.finalproject.model.CryptoCurrency
import com.finalproject.model.CryptoResponse
import com.finalproject.system.CoinSystem
import com.finalproject.utils.Utils
import com.finalproject.worker.CustomWorker
import com.google.android.material.bottomnavigation.BottomNavigationView

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    public val cryptoList = mutableListOf<CryptoCurrency>()
    private lateinit var adapter: CryptoRecyclerViewAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var cryptoCurrencyDB: CryptoCurrencyDB
    lateinit var mediaPlayer: MediaPlayer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerViewCrypto)
        recyclerView.layoutManager = LinearLayoutManager(this)

        cryptoCurrencyDB = CryptoCurrencyDB.getDatabase(this)

        adapter = CryptoRecyclerViewAdapter(this, cryptoList,2)

        fetchCryptoData()
        scheduleWorker()


        mediaPlayer = MediaPlayer.create(this, R.raw.background)
        mediaPlayer.isLooping = true
        mediaPlayer.start()


        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.page_1

        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    Toast.makeText(this, "You are already in the main page", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.page_2 -> {
                    val intent = Intent(this, WalletActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
    }
    private fun fetchCryptoData() {
        val service = ApiClient.getClient().create(ApiService::class.java)
        service.getCryptoCurrencies().enqueue(object : Callback<CryptoResponse> {
            override fun onResponse(call: Call<CryptoResponse>, response: Response<CryptoResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let { cryptoResponse ->
                        cryptoList.clear()
                        cryptoList.addAll(cryptoResponse.data) // Access the `data` field
                        CoinSystem.cryptoList = cryptoList as ArrayList<CryptoCurrency>
                        adapter.notifyDataSetChanged()
                        recyclerView.adapter = adapter
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Failed to fetch data", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<CryptoResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun scheduleWorker() {
        val workRequest = OneTimeWorkRequestBuilder<CustomWorker>()
            .setInitialDelay(60, TimeUnit.SECONDS) //
            .build()

        WorkManager.getInstance(this).enqueueUniqueWork(
            "RefreshJsonWorker",
            ExistingWorkPolicy.REPLACE,
            workRequest
        )
        adapter.notifyDataSetChanged()
        recyclerView.adapter = adapter
        // Reschedule after completion
        WorkManager.getInstance(this).getWorkInfoByIdLiveData(workRequest.id).observe(this) { workInfo ->
            if (workInfo != null && workInfo.state.isFinished) {
                scheduleWorker() // Schedule the worker again
            }
        }
    }

}

