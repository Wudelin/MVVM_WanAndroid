package com.wdl.wanandroid.worker

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class DownloadWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {

        downloadImg()

        return Result.success()
    }

    private fun downloadImg() {

    }
}