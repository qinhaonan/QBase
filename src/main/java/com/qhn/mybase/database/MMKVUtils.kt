package com.qhn.mybase.database

import com.qhn.mybase.network.gsonSingleton
import com.tencent.mmkv.MMKV
import java.lang.reflect.Type

/**
 *  Create by QinHaonan on 2021/8/5
 */
object MMKVUtils {
    val userInfoKV by lazy { MMKV.mmkvWithID("user_info") }

    fun <T> putData(key: String, value: T) {
        userInfoKV.putString(key, gsonSingleton.toJson(value))
    }

    inline fun <reified T> getData(key: String, value: String): T {
        return gsonSingleton.fromJson(userInfoKV.getString(key, value), T::class.java)
    }
}