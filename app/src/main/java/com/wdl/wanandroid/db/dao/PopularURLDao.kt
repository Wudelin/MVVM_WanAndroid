package com.wdl.wanandroid.db.dao

import androidx.lifecycle.LiveData
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
     * 返回结果为LiveData时无需使用suspend关键字修饰，否则报错：
     * http://www.chinaoc.com.cn/p/1196282.html
     */
    @Query("select * from popular_url")
    fun queryAll(): LiveData<List<PopUrlBean>>
}