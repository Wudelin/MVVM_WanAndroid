package com.wdl.wanandroid.db.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wdl.wanandroid.db.bean.PopUrlBean

/**
 * Create by: wdl at 2020/4/28 14:43
 */
@Dao
interface PopularURLDao {
    /**
     * 插入
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveArticles(urls: List<PopUrlBean>): List<Long>

    /**
     * 查询
     */
    @Query("select * from popular_url")
    suspend fun queryAll(): List<PopUrlBean>
}