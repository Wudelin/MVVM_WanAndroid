package com.wdl.wanandroid.worker

import android.content.Context
import android.os.Environment
import android.os.SystemClock
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.wdl.wanandroid.base.BASE_URL
import com.wdl.wanandroid.remote.net.RetrofitManager
import okhttp3.ResponseBody
import java.io.*

/**
 * 图片下载
 *
 * https://developer.android.google.cn/topic/libraries/architecture/workmanager/how-to/define-work
 * https://codelabs.developers.google.com/codelabs/android-workmanager/#5
 *
 */
class DownloadWorker(val context: Context, params: WorkerParameters) : Worker(context, params) {

    companion object {
        const val PATH = "PATH"
    }

    override fun doWork(): Result =
        // 从inputData中取值
        downloadImg(inputData.getString("url")!!.replace(BASE_URL, ""))

    private fun downloadImg(path: String): Result {
        RetrofitManager.wanService.downloadImg(path).execute().run {
            return if (isSuccessful) {
                val filePath = writeResponseBodyToDisk(body()!!)
                if (filePath.isNullOrEmpty()) {
                    Result.failure()
                } else {
                    Result.success(workDataOf(PATH to filePath))
                }
            } else {
                Result.failure()
            }
        }
    }


    /**
     * 下载到本地
     *
     * @param body 内容
     * @return 成功或者失败
     */
    private fun writeResponseBodyToDisk(body: ResponseBody): String? {
        return try {
            //判断文件夹是否存在
            val files =
                applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) //跟目录一个文件夹
            //创建一个文件
            val futureStudioIconFile =
                File(files, "${SystemClock.currentThreadTimeMillis()}+download.jpg")
            //初始化输入流
            var inputStream: InputStream? = null
            //初始化输出流
            var outputStream: OutputStream? = null
            try {
                //设置每次读写的字节
                val fileReader = ByteArray(4096)
                var fileSizeDownloaded: Long = 0
                //请求返回的字节流
                inputStream = body.byteStream()
                //创建输出流
                outputStream = FileOutputStream(futureStudioIconFile)
                //进行读取操作
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    //进行写入操作
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                }

                //刷新
                outputStream.flush()
                futureStudioIconFile.absolutePath
            } catch (e: IOException) {
                null
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: IOException) {
            null
        }
    }
}

