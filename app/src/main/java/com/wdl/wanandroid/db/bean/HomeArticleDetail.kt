package com.wdl.wanandroid.db.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Create by: wdl at 2020/4/14 15:19
 */
@Entity(tableName = "home_article")
data class HomeArticleDetail(
    @PrimaryKey(autoGenerate = true) val aId: Int = 0,
    @ColumnInfo(name = "chapterName") val chapterName: String,
    @ColumnInfo(name = "collect") val collect: Boolean,
    @ColumnInfo(name = "desc") val desc: String,
    @ColumnInfo(name = "fresh") val fresh: Boolean,
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "link") val link: String,
    @ColumnInfo(name = "niceDate") val niceDate: String,
    @ColumnInfo(name = "superChapterName") val superChapterName: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "type") val type: Int
)
