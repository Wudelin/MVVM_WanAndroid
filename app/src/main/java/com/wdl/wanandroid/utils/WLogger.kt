package com.wdl.wanandroid.utils

import android.text.TextUtils
import android.util.Log

/**
 * Create by: wdl at 2020/4/9 17:41
 */
object WLogger {
    private var isDebug = false
    private val TAG = WLogger::class.java.simpleName
    private var className: String? = null
    private var methodName: String? = null
    private var lineNumber = 0
    private const val STACK_TRACE_ELEMENT = 0
    fun setIsDebug(isDebug: Boolean) {
        WLogger.isDebug = isDebug
    }

    private fun getMethodInfo(stackTraces: Array<StackTraceElement>) {
        className = stackTraces[STACK_TRACE_ELEMENT].className
        methodName = stackTraces[STACK_TRACE_ELEMENT].methodName
        lineNumber = stackTraces[STACK_TRACE_ELEMENT].lineNumber
    }

    private fun createLog(msg: String): String {
        val sb = StringBuilder()
        sb.append("[")
            .append(className)
            .append(" : ")
            .append(methodName)
            .append(" : ")
            .append(lineNumber)
            .append("]")
            .append("-msg:")
            .append(msg)
        return sb.toString()
    }

    fun i(msg: String) {
        if (isDebug && isNull(msg)) {
            getMethodInfo(Throwable().stackTrace)
            Log.i(TAG, createLog(msg))
        }
    }

    fun e(msg: String) {
        if (isDebug && isNull(msg)) {
            getMethodInfo(Throwable().stackTrace)
            Log.e(TAG, createLog(msg))
        }
    }

    fun d(msg: String) {
        if (isDebug && isNull(msg)) {
            getMethodInfo(Throwable().stackTrace)
            Log.d(TAG, createLog(msg))
        }
    }

    fun w(msg: String) {
        if (isDebug && isNull(msg)) {
            getMethodInfo(Throwable().stackTrace)
            Log.w(TAG, createLog(msg))
        }
    }

    private fun isNull(msg: String): Boolean {
        return !TextUtils.isEmpty(msg)
    }
}