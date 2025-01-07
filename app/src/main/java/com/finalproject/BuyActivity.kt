package com.finalproject

import android.content.Intent
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
import com.finalproject.databinding.ActivityBuyBinding
import com.finalproject.model.CryptoCurrency
import com.finalproject.model.Wallet
import com.finalproject.system.CoinSystem

class BuyActivity : AppCompatActivity() {
    private lateinit var cryptoCurrency: CryptoCurrency

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy)

        cryptoCurrency = intent.getParcelableExtra("crypto")!!

        val tvCryptoName: TextView = findViewById(R.id.tvCryptoName)
        val etAmount: EditText = findViewById(R.id.etAmount)
        val btnBuy: Button = findViewById(R.id.btnBuy)

        tvCryptoName.text = cryptoCurrency.name

        btnBuy.setOnClickListener {
            val amountToBuy = etAmount.text.toString().toDoubleOrNull()
            if (amountToBuy == null || amountToBuy <= 0) {
                Toast.makeText(this, "Enter a valid amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val success = CoinSystem.buyCoin(cryptoCurrency.id, amountToBuy)
            if (success) {
                Toast.makeText(this, "Bought $amountToBuy ${cryptoCurrency.symbol}", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}