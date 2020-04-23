package com.wdl.wanandroid.bean

/**
 * Create by: wdl at 2020/4/13 10:09
 */
data class UserInfo(
    val admin: Boolean = false,
    val chapterTops: List<String> = emptyList(),
    val collectIds: MutableList<String> = mutableListOf(),
    val email: String = "",
    val icon: String = "",
    val id: Int,
    val nickname: String = "",
    val password: String = "",
    val token: String = "",
    val type: Int = 0,
    val username: String = ""
)