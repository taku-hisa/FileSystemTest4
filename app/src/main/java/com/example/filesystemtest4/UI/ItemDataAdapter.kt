package com.example.filesystemtest4.UI

import android.content.Context
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.filesystemtest4.R
import com.example.filesystemtest4.database.Item
import com.facebook.drawee.view.SimpleDraweeView
import java.io.File

class ItemDataAdapter(
    private val context: Context
) : PagingDataAdapter<Item, ItemDataAdapter.ItemViewHolder>(CALLBACK) {

    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val simpleDraweeView : SimpleDraweeView = itemView.findViewById(R.id.image)
        //val imageView : ImageView = itemView.findViewById(R.id.image)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        //画像表示
        val Path = context.getFilesDir().getPath() + "/" + getItem(position)!!.image
        val file = File(Path)
        val URI = Uri.fromFile(file)
        /*Glide.with(context)
            .load(URI)
            .error(android.R.drawable.ic_btn_speak_now)
            .into(holder.imageView)*/
        holder.simpleDraweeView.setImageURI(URI,context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = View.inflate(parent.context, R.layout.card_layout, null)
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