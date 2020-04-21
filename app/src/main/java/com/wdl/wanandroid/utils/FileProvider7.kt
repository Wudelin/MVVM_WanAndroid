package com.wdl.wanandroid.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import java.io.File

/**
 * Create by: wdl at 2020/4/21 15:03
 */
object FileProvider7 {
    fun getUriForFile(context: Context, file: File?): Uri? {
        return if (Build.VERSION.SDK_INT >= 24) {
            getUriForFile24(context, file)
        } else {
            Uri.fromFile(file)
        }
    }

    private fun getUriForFile24(context: Context, file: File?): Uri {
        return FileProvider.getUriForFile(
            context,
            context.packageName + ".android7.fileprovider",
            file!!
        )
    }

    fun setIntentDataAndType(
        context: Context,
        intent: Intent,
        type: String?,
        file: File?,
        writeAble: Boolean
    ) {
        if (Build.VERSION.SDK_INT >= 24) {
            intent.setDataAndType(getUriForFile(context, file), type)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            if (writeAble) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            }
        } else {
            intent.setDataAndType(Uri.fromFile(file), type)
        }
    }
}