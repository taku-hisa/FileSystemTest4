package com.example.filesystemtest4.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//Entityの指定
@Database(entities = [Item::class], version = 1, exportSchema = false)
abstract class ItemRoomDatabase: RoomDatabase() {
    //DAOの指定
    abstract fun itemDao() : ItemDao
    //DBのビルド
    companion object {
        fun buildDatabase(context: Context): ItemRoomDatabase {
            return Room.databaseBuilder(
                context,
                ItemRoomDatabase::class.java, "item_db"
            ).build()
        }
    }
}