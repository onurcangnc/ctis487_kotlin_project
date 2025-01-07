package com.finalproject

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptotradingapp.adapter.CryptoRecyclerViewAdapter
import com.finalproject.databinding.ActivityWalletBinding
import com.finalproject.model.Asset
import com.finalproject.system.CoinSystem
import com.google.android.material.bottomnavigation.BottomNavigationView

class WalletActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWalletBinding
    private lateinit var adapter: CryptoRecyclerViewAdapter
    private var assetList = mutableListOf<Asset>() // List to store user's assets

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWalletBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        loadAssets()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


        bottomNavigationView.selectedItemId = R.id.page_2
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.page_1 -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.page_2 -> {
                    // Already in WalletActivity, show a Toast and don't start a new intent
                    Toast.makeText(this, "You are already in the wallet page", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }



    private fun setupUI() {
        // Set up RecyclerView
        binding.recyclerViewCrypto.layoutManager = LinearLayoutManager(this)
        adapter = CryptoRecyclerViewAdapter(this, assetList, CryptoRecyclerViewAdapter.TYPE_ASSET_ITEM)
        binding.recyclerViewCrypto.adapter = adapter

        // Set balance in the header
        val wallet = CoinSystem.getWallet()
        binding.balanceAmount.text = "$${String.format("%.2f", wallet.amount)}"
    }

    private fun loadAssets() {
        // Fetch assets from CoinSystem
        assetList = CoinSystem.displayAssets().toMutableList()

        // Update RecyclerView
        adapter.updateData(assetList)
    }
}