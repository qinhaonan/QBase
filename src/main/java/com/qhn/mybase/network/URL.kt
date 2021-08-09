package com.qhn.mybase.network

import retrofit2.Retrofit

const val BASE_URL = "https://api.douban.com/v2/movie/"
const val BASE_URL_MOCK = "http://rest.apizza.net/mock/2d454eeb8f61165fa3fcddbdc83d8548/"
//const val BASE_URL_MOCK = "http://rest.apizza.net/mock/2d454eeb8f61165fa3fcddbdc83d8548/blackhouse/"

val apiProvider: Retrofit by lazy { buildConfig(BASE_URL_MOCK) }