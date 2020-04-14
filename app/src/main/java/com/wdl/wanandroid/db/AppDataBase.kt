package com.wdl.wanandroid.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.wdl.wanandroid.App
import com.wdl.wanandroid.base.DB_NAME
import com.wdl.wanandroid.db.bean.HomeArticleDetail
import com.wdl.wanandroid.db.dao.HomeArticleDao
import com.wdl.wanandroid.utils.WLogger

/**
 * Create by: wdl at 2020/4/14 15:10
 * 数据库相关
 */
@Database(entities = [HomeArticleDetail::class], version = 1, exportSchema = false)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getHomeArticleDao(): HomeArticleDao

    companion object {
        private val instance: AppDataBase by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Room.databaseBuilder(App.app, AppDataBase::class.java, DB_NAME).addCallback(object :
                Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    WLogger.e("Create $DB_NAME")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    WLogger.e("Open $DB_NAME")
                }
            }).build()
        }

        fun getHomeArticleDao() = instance.getHomeArticleDao()
    }
}