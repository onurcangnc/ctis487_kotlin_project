package com.finalproject.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.finalproject.model.Asset
import com.finalproject.model.CryptoCurrency
import com.finalproject.model.FavCoin
import com.finalproject.model.Transaction
import com.finalproject.model.Wallet
import com.finalproject.utils.Converters
import com.finalproject.utils.Utils

@Database(
    entities = [Asset::class, Wallet::class, Transaction::class, FavCoin::class],
    version = 4)
@TypeConverters(Converters::class)
abstract class CryptoCurrencyDB : RoomDatabase() {
    abstract fun AssetDAO(): AssetDAO
    abstract fun TransactionDAO(): TransactionDAO
    abstract fun WalletDAO(): WalletDAO
    abstract fun FavCoinDAO(): FavCoinDAO

    companion object{
        @Volatile  //it makes that instance to visible to other threads
        private var INSTANCE: CryptoCurrencyDB?=null

        fun getDatabase(context:Context): CryptoCurrencyDB {
            val tempInstance = INSTANCE
            if(tempInstance !=null){
                return  tempInstance
            }
            /*
            everthing in this block protected from concurrent execution by multiple threads.In this block database instance is created
            same database instance will be used. If many instance are used, it will be so expensive
             */
            val instance: CryptoCurrencyDB by lazy {
                Room.databaseBuilder(context, CryptoCurrencyDB::class.java, Utils.DATABASENAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            INSTANCE = instance
            return instance
        }
    }
}




//
//@Database(
//    entities = [Person::class, Movie::class, Writer::class, Book::class],
//    version = 1,
//    exportSchema = false
//)
//abstract class BookRoomDatabase : RoomDatabase() {
//
//    abstract fun writerDAO(): WriterDAO
//
//    abstract fun bookDAO(): BookDAO
//
//    abstract fun personDAO(): PersonDAO
//
//    abstract fun movieDAO(): MovieDAO
//
//    companion object{
//        @Volatile  //it makes that instance to visible to other threads
//        private var INSTANCE: BookRoomDatabase?=null
//
//        fun getDatabase(context:Context): BookRoomDatabase {
//            val tempInstance = INSTANCE
//            if(tempInstance !=null){
//                return  tempInstance
//            }
//            /*
//            everthing in this block protected from concurrent execution by multiple threads.In this block database instance is created
//            same database instance will be used. If many instance are used, it will be so expensive
//             */
//            val instance: BookRoomDatabase by lazy {
//                Room.databaseBuilder(context, BookRoomDatabase::class.java, Utils.DATABASENAME)
//                    .allowMainThreadQueries()
//                    .fallbackToDestructiveMigration()
//                    .build()
//            }
////            val  instance =Room.databaseBuilder(context.applicationContext, BookRoomDatabase::class.java, Utils.DATABASENAME).build()
//            INSTANCE = instance
//            return instance
//
//        }
//
//    }