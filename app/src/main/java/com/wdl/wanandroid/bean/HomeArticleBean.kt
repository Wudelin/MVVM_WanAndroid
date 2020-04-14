package com.wdl.wanandroid.bean

import com.wdl.wanandroid.db.bean.HomeArticleDetail

/**
 * Create by: wdl at 2020/4/14 15:04
 */

data class HomeArticleRes(
    val `data`: ArticleData,
    val errorCode: Int,
    val errorMsg: String
)

data class ArticleData(
    val curPage: Int,
    val offset: Int,
    val over: Boolean,
    val pageCount: Int,
    val size: Int,
    val total: Int,
    val datas: List<HomeArticleDetail>
)