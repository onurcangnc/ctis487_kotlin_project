package com.finalproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.finalproject.databinding.ActivityCoinDetailsBinding
import com.finalproject.model.CryptoCurrency
import com.finalproject.system.CoinSystem
import com.google.android.material.bottomnavigation.BottomNavigationView

class CoinDetailsActivity : AppCompatActivity() {
    lateinit var binding: ActivityCoinDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCoinDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val crypto = intent.getParcelableExtra<CryptoCurrency>("crypto")
        binding.stockName.text = crypto?.name
        binding.stockCompany.text = crypto?.symbol
        binding.stockPrice.text = String.format("$%.2f", crypto?.quote!!.USD.price)
        binding.stockChangePercent.text =
            String.format("%.2f%%", crypto?.quote!!.USD.percent_change_24h)
        binding.marketCap.text =
            "Market Cap: $${String.format("%.2f", crypto?.quote!!.USD.market_cap)}"
        binding.volume24h.text =
            "24h Volume: $${String.format("%.2f", crypto?.quote!!.USD.volume_24h)}"

        if (crypto.quote.USD.percent_change_24h >= 0) {
            binding.stockChangePercent.setTextColor(Color.GREEN)
        } else {
            binding.stockChangePercent.setTextColor(Color.RED)
        }

        binding.buyButton.setOnClickListener {
            Toast.makeText(this, "Buy ${crypto.name}", Toast.LENGTH_SHORT).show()
            Log.d("BUY", "Buy button clicked")
            var intent = Intent(this, BuyActivity::class.java)
            intent.putExtra("crypto", crypto)
            startActivity(intent)
        }

        binding.sellButton.setOnClickListener {
            Toast.makeText(this, "Sell ${crypto.name}", Toast.LENGTH_SHORT).show()
            Log.d("SELL", "Sell button clicked")
            var intent = Intent(this, SellActivity::class.java)
            intent.putExtra("crypto", crypto)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)


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
}

