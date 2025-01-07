package com.example.cryptotradingapp.adapter

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.finalproject.BuyActivity
import com.finalproject.CoinDetailsActivity
import com.finalproject.R
import com.finalproject.model.Asset
import com.finalproject.model.CryptoCurrency
import com.finalproject.system.CoinSystem
import com.finalproject.system.CoinSystem.viewCoin

//1: asset, 2: Crypto
class CryptoRecyclerViewAdapter(
    private val context: Context,
    private var itemList: List<Any>, // Accepts a list of type Any
    private val type: Int // Determines if the adapter is for CryptoCurrency or Asset
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        const val TYPE_CRYPTO_ITEM = 1
        const val TYPE_ASSET_ITEM = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            TYPE_CRYPTO_ITEM -> {
                val view = inflater.inflate(R.layout.item_crypto_row, parent, false)
                CryptoViewHolder(view)
            }

            TYPE_ASSET_ITEM -> {
                val view = inflater.inflate(R.layout.item_asset, parent, false)
                AssetViewHolder(view)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when (holder) {
            is CryptoViewHolder -> holder.bind(item as CryptoCurrency)
            is AssetViewHolder -> holder.bind(item as Asset)
            else -> throw IllegalArgumentException("Unknown ViewHolder type")
        }
//        holder.itemView.setOnClickListener {
//            val animationView = holder.itemView.findViewById<LottieAnimationView>(R.id.lottieAnimation)
//            animationView.visibility = View.VISIBLE
//            animationView.playAnimation()
//
//            animationView.addAnimatorListener(object : Animator.AnimatorListener {
//                override fun onAnimationStart(animation: Animator) {
//                    // Optional: Add any logic if needed when animation starts
//                }
//
//                override fun onAnimationEnd(animation: Animator) {
//                    animationView.visibility = View.GONE
//                    // Perform action after animation ends
//                    val intent = Intent(context, CoinDetailsActivity::class.java)
//                    intent.putExtra("crypto", itemList[position] as CryptoCurrency)
//                    context.startActivity(intent)
//                }
//
//                override fun onAnimationCancel(animation: Animator) {
//                    // Optional: Handle animation cancellation if needed
//                }
//
//                override fun onAnimationRepeat(animation: Animator) {
//                    // Optional: Handle animation repeat if needed
//                }
//            })
//        }
//        holder.itemView.setOnLongClickListener {
//            val intent = Intent(context, BuyActivity::class.java)
//            intent.putExtra("crypto", itemList[position] as CryptoCurrency)
//            context.startActivity(intent)
//            true
//        }


    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return when (itemList[position]) {
            is CryptoCurrency -> TYPE_CRYPTO_ITEM
            is Asset -> TYPE_ASSET_ITEM
            else -> throw IllegalArgumentException("Unknown item type at position $position")
        }
    }


    fun updateData(newItemList: List<Any>) {
        itemList = newItemList
        notifyDataSetChanged()
    }

    inner class CryptoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgCrypto: ImageView = view.findViewById(R.id.imgCrypto)
        private val tvName: TextView = view.findViewById(R.id.tvCryptoName)
        private val tvSymbol: TextView = view.findViewById(R.id.tvCryptoSymbol)
        private val tvPrice: TextView = view.findViewById(R.id.tvCryptoPrice)
        private val tvChange: TextView = view.findViewById(R.id.tvCryptoChange)

        fun bind(crypto: CryptoCurrency) {
            tvName.text = crypto.name
            tvSymbol.text = crypto.symbol
            tvPrice.text = "$${String.format("%.3f", crypto.quote.USD.price)}"
            tvChange.text = "${String.format("%.2f", crypto.quote.USD.percent_change_24h)}%"

            // Dynamically set background color based on 24h change rate
            val changeRate = crypto.quote.USD.percent_change_24h
            if (changeRate >= 0) {
                itemView.setBackgroundColor(Color.parseColor("#E0F7E9")) // Light green for positive change
                tvChange.setTextColor(Color.parseColor("#2E7D32")) // Dark green for text
            } else {
                itemView.setBackgroundColor(Color.parseColor("#FFEBEE")) // Light red for negative change
                tvChange.setTextColor(Color.parseColor("#C62828")) // Dark red for text
            }

            val imageUrl =
                "https://assets.coincap.io/assets/icons/${crypto.symbol.lowercase()}@2x.png"
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.default_crypto)
                .error(R.drawable.default_crypto)
                .into(imgCrypto)

            itemView.setOnClickListener {
                val animationView: LottieAnimationView = itemView.findViewById(R.id.lottieAnimation)
                animationView.visibility = View.VISIBLE
                animationView.playAnimation()

                animationView.addAnimatorListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {}

                    override fun onAnimationEnd(animation: Animator) {
                        animationView.visibility = View.GONE
                        val intent = Intent(context, CoinDetailsActivity::class.java)
                        intent.putExtra("crypto", crypto)
                        context.startActivity(intent)
                    }

                    override fun onAnimationCancel(animation: Animator) {}

                    override fun onAnimationRepeat(animation: Animator) {}
                })
            }

            // Handle long click for direct navigation to BuyActivity
            itemView.setOnLongClickListener {
                val intent = Intent(context, BuyActivity::class.java)
                intent.putExtra("crypto", crypto)
                context.startActivity(intent)
                true
            }
        }
    }



    inner class AssetViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imgCrypto: ImageView = view.findViewById(R.id.imgTransaction)
        private val tvName: TextView = view.findViewById(R.id.tvTransactionName)
        private val tvTransactionAmount: TextView = view.findViewById(R.id.tvTransactionAmount)
        private val tvTransactionTotal: TextView = view.findViewById(R.id.tvTransactionTotal)
        private val tvTransactionPrice: TextView = view.findViewById(R.id.tvTransactionPrice)

        fun bind(asset: Asset) {
            val crypto = viewCoin(asset.cryptoCurrencyId)!!
            tvName.text = crypto.name
            tvTransactionAmount.text = "${String.format("%.3f", asset.amount)} ${crypto.symbol}"
            tvTransactionTotal.text = "$${String.format("%.3f", crypto.quote.USD.price * asset.amount)}"
            tvTransactionPrice.text = "$${String.format("%.2f", crypto.quote.USD.price)}"

            val imageUrl = "https://assets.coincap.io/assets/icons/${crypto.symbol.lowercase()}@2x.png"
            Glide.with(context)
                .load(imageUrl)
                .placeholder(R.drawable.default_crypto)
                .error(R.drawable.default_crypto)
                .into(imgCrypto)
        }

    }

    // Helper function to fetch a cryptocurrency by ID (assumes you have a CoinSystem or similar class)
    private fun viewCoin(id: Int): CryptoCurrency? {
        return CoinSystem.cryptoList.find { it.id == id }
    }
}
