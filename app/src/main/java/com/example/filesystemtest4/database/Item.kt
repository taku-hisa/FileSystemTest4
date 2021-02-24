package com.example.filesystemtest4.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob

@Entity(tableName = "item_table")
data class Item(
    @PrimaryKey(autoGenerate = true) val id: Int, //判別ID
    @ColumnInfo
    val category: String, //カテゴリ
    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val image: ByteArray, //画像
    @ColumnInfo
    val detail: String //詳細
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Item

        if (id != other.id) return false
        if (category != other.category) return false
        if (!image.contentEquals(other.image)) return false
        if (detail != other.detail) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + category.hashCode()
        result = 31 * result + image.contentHashCode()
        result = 31 * result + detail.hashCode()
        return result
    }
}