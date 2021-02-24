package com.example.filesystemtest4.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ItemDao {
    //カテゴリのItemを表示する
    @Query("SELECT * FROM item_table WHERE category = :Category")
    fun getItem(Category : String): PagingSource<Int,Item>
    //Itemを新規追加する
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem (item:Item)
}