package com.qhn.mybase.network

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

/**
 *  Create by QinHaonan on 2018/10/18
 */
class NullStringToEmptyAdapterFactory<T> : TypeAdapterFactory {
    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T>? {
        val rawType = type?.rawType as Class<*>
        if (rawType != String::class.java) {
            return null
        }
        return StringNullAdapter() as TypeAdapter<T>?
    }

    class StringNullAdapter : TypeAdapter<String>() {
        override fun write(writer: JsonWriter?, value: String?) {
            if (value == null) {
                writer?.nullValue()
                return
            }
            writer?.value(value)
        }

        override fun read(reader: JsonReader?): String? {
            if (reader?.peek() == JsonToken.NULL) {
                reader.nextNull()
                return ""
            }
            return reader?.nextString()
        }

    }
}


