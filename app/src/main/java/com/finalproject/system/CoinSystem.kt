package com.finalproject.system

import android.util.Log
import androidx.room.Insert
import androidx.room.Room
import com.finalproject.MainActivity
import com.finalproject.db.CryptoCurrencyDB
import com.finalproject.model.Asset
import com.finalproject.model.CryptoCurrency
import com.finalproject.model.Transaction
import com.finalproject.model.TransactionType
import com.finalproject.model.Wallet
import com.finalproject.utils.Utils

object CoinSystem {
    private val cryptoCurrencyDB = CryptoCurrencyDB.getDatabase(MainActivity())
    var cryptoList = ArrayList<CryptoCurrency>()
    fun getWallet(): Wallet{
        val dao = CryptoCurrencyDB.getDatabase(MainActivity()).WalletDAO()
        var instance: Wallet
        val instanceList = dao.getWallet()

        if(instanceList.isEmpty()){
            instance = Wallet()
            dao.insertWallet(instance)
        }
        if(instanceList.size > 1){
            dao.deleteAll()
            instance = Wallet()
            dao.insertWallet(instance)
        }
        else{
            instance = instanceList[0]
        }
        return instance
    }

    fun updateWallet(wallet: Wallet, amount: Double){
        val dao = CryptoCurrencyDB.getDatabase(MainActivity()).WalletDAO()
        wallet.amount += amount
        dao.updateWallet(wallet)
    }

    //TO-DO
    fun addTransaction(coinId: Int, amount:Double, type: TransactionType){
        val dao = CryptoCurrencyDB.getDatabase(MainActivity()).TransactionDAO()
        val coin = viewCoin(coinId)
        if (coin == null) {
            println("Error: Cryptocurrency not found or price unavailable.")
            return
        }
        val transaction = Transaction(coinId = coinId, amount = amount, transactionType = type, pricePerCoin = coin.quote.USD.price)
        dao.insertTransaction(transaction)
    }

    fun getAllTransaction(): List<Transaction>{
        val dao = CryptoCurrencyDB.getDatabase(MainActivity()).TransactionDAO()
        return dao.getAllTransaction()
    }


    fun displayAssets(): List<Asset>{
        val dao = CryptoCurrencyDB.getDatabase(MainActivity()).AssetDAO()
        return dao.getAllAsset()
    }

    fun getCoinImageURL(currency: CryptoCurrency): String{
        return "https://assets.coincap.io/assets/icons/${currency.quote}@2x.png"
    }
    //TO-DO
    fun viewCoin(id:Int): CryptoCurrency?{
        return cryptoList.find { it.id == id }
    }

    fun buyCoin(coinId:Int, amount:Double): Boolean{
        val budget = getWallet().amount
        val currency = viewCoin(coinId)

        if(amount<=0){
            println("Invalid amount. Please enter a positive value.")
            return false
        }

        if (currency == null) {
            println("Error: Cryptocurrency not found or price unavailable.")
            return false
        }
        val totalCost = currency.quote.USD.price * amount

        if(totalCost > budget){
            println("Insufficient funds. You need $${"%.2f".format(totalCost - budget)} more to complete this purchase.")
            return false
        }

        updateWallet(getWallet(), -totalCost)
        addTransaction(coinId, amount, TransactionType.BUY)
        addAsset(coinId, amount)
        Log.d("TRANSACTION","TRANSACTION SIZE: ${getAllTransaction().size}")
        return true
    }

    fun getFavCoinList():List<CryptoCurrency>{
        val dao = CryptoCurrencyDB.getDatabase(MainActivity()).FavCoinDAO()
        val favCoins = dao.getAllFavCoin()

        val favCoinList = ArrayList<CryptoCurrency>()
        for(favCoin in favCoins) {
            val coin = viewCoin(favCoin)
            if (coin != null) {
                favCoinList.add(coin)
            }
        }
        return favCoinList
    }

    fun searchCoin(query: String): List<CryptoCurrency> {
        return cryptoList.filter { it.name.contains(query, ignoreCase = true) }
    }


    fun addAsset(cryptoCurrencyId: Int, amount: Double){
        val dao = CryptoCurrencyDB.getDatabase(MainActivity()).AssetDAO()
        val asset: Asset = dao.getAssetByCryptoCurrencyId(cryptoCurrencyId)
        if(asset == null){
            Log.d("ASSET", "New Asset Created")
            val newAsset = Asset(cryptoCurrencyId = cryptoCurrencyId, amount = amount)
            dao.insertAsset(newAsset)
        }else{
            Log.d("ASSET", "Asset Updated")
            asset.amount += amount
            dao.updateAsset(asset)
        }
    }

    fun sellAsset(cryptoCurrencyId: Int, amount: Double): Boolean{
        Log.d("SELL", "SELLING ASSET")
        val dao = CryptoCurrencyDB.getDatabase(MainActivity()).AssetDAO()
        val transactionDao = CryptoCurrencyDB.getDatabase(MainActivity()).TransactionDAO()

        val asset = dao.getAssetByCryptoCurrencyId(cryptoCurrencyId)

        if(asset.amount < amount){
            Log.d("SELL","Insufficient amount of asset. You only have ${asset.amount} of this asset.")
            return false
        }

        //decrement amount
        asset.amount -= amount

        //delete asset if amount becomes zero
        if(0 >= asset.amount){
            dao.deleteAsset(asset)
        }else{
            dao.updateAsset(asset)
        }

        //Create transaction
        val transaction = viewCoin(asset.cryptoCurrencyId)?.quote?.USD?.let { Transaction(coinId = cryptoCurrencyId, amount = amount, transactionType = TransactionType.SELL, pricePerCoin = it.price) }
        if (transaction != null) {
            transactionDao.insertTransaction(transaction)
        }

        //update wallet
        updateWallet(getWallet(), asset.amount * (viewCoin(cryptoCurrencyId)?.quote?.USD?.price!!))
        return true
    }
}