package com.qhn.mybase.utils

import android.content.Context
import java.util.*


/**
 *  SharedPreferences 工具类
 */
const val LOCATION = "LOCATION"
val SEARCH_HISTORY = "SEARCH_HISTORY"

fun putSp(context: Context, key: String, value: Any, tag: String) {
    val sp = context.getSharedPreferences(tag,
            Context.MODE_PRIVATE)
    val editor = sp.edit()

    when (value) {
        is String -> editor.putString(key, value)
        is Int -> editor.putInt(key, value)
        is Boolean -> editor.putBoolean(key, value)
        is Float -> editor.putFloat(key, value)
        is Long -> editor.putLong(key, value)
        else -> editor.putString(key, value.toString())
    }
    editor.apply()
}

fun getSp(context: Context, key: String, value: Any, tag: String): Any? {
    val sp = context.getSharedPreferences(tag, Context.MODE_PRIVATE)
    return when (value) {
        is String -> sp.getString(key, value)
        is Int -> sp.getInt(key, value)
        is Boolean -> sp.getBoolean(key, value)
        is Float -> sp.getFloat(key, value)
        is Long -> sp.getLong(key, value)
        else -> null
    }

}

fun removeSp(context: Context, key: String, tag: String) {
    val sp = context.getSharedPreferences(tag,
            Context.MODE_PRIVATE)
    sp.edit().remove(key).apply()
}

fun removeAllSp(context: Context, tag: String) {
    val sp = context.getSharedPreferences(tag,
            Context.MODE_PRIVATE)
    sp.edit().clear().apply()
}

fun containsSp(context: Context, key: String, tag: String): Boolean {
    val sp = context.getSharedPreferences(tag,
            Context.MODE_PRIVATE)
    return sp.contains(key)
}

fun getAll(context: Context, tag: String): Map<String, *> {
    val sp = context.getSharedPreferences(tag,
            Context.MODE_PRIVATE)
    return sp.all
}

fun getAllByTime(context: Context, tag: String): NavigableMap<String, Any?> {
    val map = getAll(context, tag)
    return TreeMap(map).descendingMap()
}

//搜索历史用,key=存入时间,
fun putNoRepeat(context: Context, value1: Any, tag: String) {
    val historyMaps = getAll(context, tag)
    for ((key, value) in historyMaps) {
        if (value == value1) {
            removeSp(context, key, tag)
        }
    }
    putSp(context, System.currentTimeMillis().toString(), value1, tag)
}

