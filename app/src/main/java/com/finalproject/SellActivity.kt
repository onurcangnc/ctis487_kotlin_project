package com.finalproject

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.finalproject.databinding.ActivitySellBinding
import com.finalproject.model.CryptoCurrency
import com.finalproject.system.CoinSystem

class SellActivity : AppCompatActivity() {
    private lateinit var cryptoCurrency: CryptoCurrency
    private var ownedAmount: Double = 0.0 // Holds the user's owned amount of the cryptocurrency

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sell)

        // Get the CryptoCurrency object from the intent
        cryptoCurrency = intent.getParcelableExtra("crypto")!!

        val tvCryptoName: TextView = findViewById(R.id.tvCryptoName)
        val tvOwnedAmount: TextView = findViewById(R.id.tvOwnedAmount)
        val etAmount: EditText = findViewById(R.id.etAmount)
        val btnSell: Button = findViewById(R.id.btnSell)

        tvCryptoName.text = cryptoCurrency.name

        // Fetch the owned amount from the database
        ownedAmount = getOwnedAmount(cryptoCurrency.id)
        tvOwnedAmount.text = "You own: ${String.format("%.3f", ownedAmount)} ${cryptoCurrency.symbol}"

        btnSell.setOnClickListener {
            val amountToSell = etAmount.text.toString().toDoubleOrNull()
            if (amountToSell == null || amountToSell <= 0) {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (amountToSell > ownedAmount) {
                Toast.makeText(this, "Insufficient assets. You own only ${String.format("%.3f", ownedAmount)} ${cryptoCurrency.symbol}", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = CoinSystem.sellAsset(cryptoCurrency.id, amountToSell)
            if (success) {
                Toast.makeText(this, "Sold $amountToSell ${cryptoCurrency.symbol}", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Helper function to get the owned amount of the cryptocurrency
    private fun getOwnedAmount(cryptoCurrencyId: Int): Double {
        val asset = CoinSystem.displayAssets().find { it.cryptoCurrencyId == cryptoCurrencyId }
        return asset?.amount ?: 0.0 // Return 0 if the user doesn't own any
    }
}
