package com.qhn.mybase.network

import com.qhn.mybase.utils.d
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
import okhttp3.*
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

fun createRetrofitService() {

}

fun buildConfig(url: String) = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(ConverterFactory())
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(OkHttpClient.Builder()
                .apply { addInterceptor(BaseInterceptor()) }
                .build())
        .build()

//配置Gson
fun buildGson(): Gson {
    val gson = GsonBuilder().create()
    return gson
}


//转换器工厂
class ConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(type: Type, annotations: Array<Annotation>, retrofit: Retrofit): Converter<ResponseBody, *>? {
        return ResponseBodyConverterToGson(buildGson(), TypeToken.get(type))
    }

    override fun requestBodyConverter(type: Type, parameterAnnotations: Array<Annotation>, methodAnnotations: Array<Annotation>, retrofit: Retrofit): Converter<*, RequestBody>? {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }


}
//拦截器
class BaseInterceptor: Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val proceed = chain.proceed(chain.request().newBuilder().build())
        d(proceed.request())
        return proceed
    }

}


//自定义responsebody 转换器
class ResponseBodyConverterToGson(private val gson: Gson, private val type: TypeToken<*>) : Converter<ResponseBody, Any> {
    override fun convert(value: ResponseBody): Any {
        val parse: JsonElement?
        val json = value.string()
        val code: Int?
        val msg: String?
        try {
            parse = JsonParser().parse(json)
            code = parse?.asJsonObject?.get("code")?.asInt
            msg = parse?.asJsonObject?.get("message")?.asString
        } catch (e: Exception) {
            throw ErrorException(ErrorCode.JSON, "数据格式异常 解析失败了", null)
        }
        if (code == null || code == ErrorCode.OK) {
            try {
                //解析全部
                return gson.fromJson<Any>(parse, type.type)
            } catch (e: Exception) {
                //Json解析错误
                throw ErrorException(ErrorCode.JSON, "数据格式异常 解析失败了", null)
            }
        } else if (code == ErrorCode.TOKEN) {
            //Token过期或者不存在
//            BaseDatabase.instant.runDataBaseTransition {
//                getTokenDao().inset(TokenEntity())
//                getTokenDao().nukeTable()
//            }
            throw ErrorException(code, msg ?: "登录过期 请重新登录", null)
        } else {
            d(json)
            //其他错误
            throw ErrorException(code, msg ?: "其他错误", json)
        }

    }
}

// 错误异常格式
data class ErrorException(val code: Int, override val message: String, val responseContent: String? = null) : Exception()

//返回格式
data class Result<T>(
        val code: Int,
        val msg: String,
        val data: T
)

//错误码
class ErrorCode {
    companion object {
        const val OK = 2000        //请求成功
        const val NETWORK = 4000   //没有网络
        const val JSON = 4001      //json解析失败
        const val EMPTY = 4002     //没有数据
        const val PARAM = 4003     //请求参数异常
        const val TOKEN = 4004     //Token 过期
        const val UNKNOWN = 4005     //未知错误
    }

}