package com.wdl.wanandroid.db.dao

import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.wdl.wanandroid.db.bean.HomeArticleDetail

/**
 * Create by: wdl at 2020/4/14 15:32
 */
@Dao
interface HomeArticleDao {
    /**
     * 插入
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticles(articles: List<HomeArticleDetail>): List<Long>

    /**
     * 查询
     */
    @Query("select * from home_article")
    fun queryAll(): MutableLiveData<List<HomeArticleDetail>>

    /**
     * 删除
     */
    @Query("delete from home_article")
    fun deleteAll(): Int
}