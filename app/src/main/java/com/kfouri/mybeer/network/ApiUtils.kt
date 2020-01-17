package com.kfouri.mybeer.network

object ApiUtils {

    val BASE_URL = "http://kfouri.onlinewebshop.net/"

    val apiService: APIService
        get() = RetrofitClient.getClient(BASE_URL)!!.create(APIService::class.java)
}