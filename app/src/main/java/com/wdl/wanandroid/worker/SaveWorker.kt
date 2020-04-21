package com.wdl.wanandroid.worker

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.wdl.wanandroid.utils.FileProvider7
import com.wdl.wanandroid.worker.DownloadWorker.Companion.PATH
import java.io.File

/**
 * Create by: wdl at 2020/4/21 10:49
 * 保存图库并通知刷新
 */
class SaveWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result =
        notifyRefresh(inputData.getString(PATH)!!)


    private fun notifyRefresh(path: String): Result {

        try {
            val bitmap = BitmapFactory.decodeStream(
                applicationContext.contentResolver.openInputStream(
                    FileProvider7.getUriForFile(
                        applicationContext,
                        File(path)
                    )!!
                )
            )

            // 插入图库
            val imageUrl = MediaStore.Images.Media.insertImage(
                applicationContext.contentResolver, bitmap, "Title", "DES"
            )

            // 最后通知图库更新
            val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
            val fileUri = FileProvider7.getUriForFile(applicationContext, File(imageUrl))
            intent.data = fileUri
            applicationContext.sendBroadcast(intent)

            return Result.success()

        } catch (e: Exception) {
            return Result.failure()
        }
    }
}