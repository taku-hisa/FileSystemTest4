package com.example.filesystemtest4.UI

import android.content.Context
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filesystemtest4.R
import com.example.filesystemtest4.database.Item

class ItemDataAdapter(
    private val context : Context
) : PagingDataAdapter<Item, ItemDataAdapter.ItemViewHolder>(CALLBACK) {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val imageView : ImageView = itemView.findViewById(R.id.image)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val imageResource = BitmapFactory.decodeByteArray(getItem(position)!!.image,0,getItem(position)!!.image.size)
        //画像表示
        Glide.with(context)
            .load(imageResource)
            .error(android.R.drawable.ic_btn_speak_now)
            .into(holder.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = View.inflate(parent.context, R.layout.card_layout,null)
        return ItemViewHolder(itemView)
    }
}

val CALLBACK = object : DiffUtil.ItemCallback<Item>() {

    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}