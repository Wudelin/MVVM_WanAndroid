package com.wdl.wanandroid.utils

import android.os.Parcelable
import com.tencent.mmkv.MMKV
import java.lang.IllegalArgumentException
import java.util.*

/**
 * Create by: wdl at 2020/4/10 10:29
 * 腾讯MMKV
 * https://github.com/Tencent/MMKV/wiki/android_tutorial_cn
 */
object MMKVUtil {
    private val instance: MMKV by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
        MMKV.defaultMMKV()
    }

    /**
     * 是否包含KEY
     */
    fun contains(key: String) = instance.containsKey(key)

    /**
     * 保存操作
     * 1.包括以下6中基本类型
     * 2.Set<String>以及Parcelable另外处理
     */
    fun put(key: String, value: Any): Boolean {
        return when (value) {
            is Int -> instance.encode(key, value)
            is Boolean -> instance.encode(key, value)
            is String -> instance.encode(key, value)
            is Float -> instance.encode(key, value)
            is Double -> instance.encode(key, value)
            is ByteArray -> instance.encode(key, value)
            else -> throw IllegalArgumentException()
        }
    }

    fun put(key: String, set: Set<String>): Boolean = instance.encode(key, set)

    fun <T : Parcelable> put(key: String, value: T): Boolean = instance.encode(key, value)

    /**
     * 取操作
     * 1.包括以下6中基本类型
     * 2.Set<String>以及Parcelable另外处理
     */
    fun get(key: String, defaultValue: Any): Any {
        return when (defaultValue) {
            is Int -> instance.decodeInt(key, defaultValue)
            is Boolean -> instance.decodeBool(key, defaultValue)
            is String -> instance.decodeString(key, defaultValue)
            is Float -> instance.decodeFloat(key, defaultValue)
            is Double -> instance.decodeDouble(key, defaultValue)
            is ByteArray -> instance.decodeBytes(key, defaultValue)
            else -> throw IllegalArgumentException()
        }
    }

    fun get(key: String): MutableSet<String> = instance.decodeStringSet(key, Collections.emptySet())

    fun <T : Parcelable> get(key: String, t: Class<T>): Parcelable =
        instance.decodeParcelable(key, t)

    /**
     * 移除
     */
    fun remove(key: String) {
        if (contains(key)) {
            instance.removeValueForKey(key)
        }
    }

    /**
     * 清空
     */
    fun clear() {
        instance.clearAll()
    }
}