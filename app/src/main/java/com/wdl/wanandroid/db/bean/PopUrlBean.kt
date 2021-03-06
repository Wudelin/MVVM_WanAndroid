package com.wdl.wanandroid.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 热门网址实体类
 */
@Entity(tableName = "popular_url")
data class PopUrlBean(
    @ColumnInfo(name = "icon") val icon: String? = "",
    @PrimaryKey(autoGenerate = false) val id: Int,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "order") val order: Int,
    @ColumnInfo(name = "visible") val visible: Int
)