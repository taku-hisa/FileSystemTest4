package com.example.filesystemtest4.UI

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import com.example.filesystemtest4.database.Item
import com.example.filesystemtest4.database.ItemDao
import com.example.filesystemtest4.database.ItemRoomDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application){
    private val dao: ItemDao
    init {
        val db = ItemRoomDatabase.buildDatabase(application)
        dao = db.itemDao()
    }
    //取得
    private fun getItem(category:String): PagingSource<Int, Item> {
        return dao.getItem(category)
    }
    //追加
    fun insertItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertItem(item)
        }
    }
    //取得したものをFlowにする
    fun getStream(category:String): Flow<PagingData<Item>> {

        return Pager(
                //pageSizeが小さすぎると読込回数が多くなる(=重い処理)ので、クラッシュすることが多くなる。
                //他方、pageSizeが大きすぎると読込するデータが大きくなるので、表示まで時間がかかる。
                //何度もテストを行い、適度な大きさに調節しましょう。
            config = PagingConfig(pageSize = 20),
            initialKey = 0
        ) {
            getItem(category)
        }.flow
    }
}
