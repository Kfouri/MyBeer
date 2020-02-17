package com.kfouri.mybeer.network

import com.kfouri.mybeer.network.model.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import rx.Observable

interface APIService {

    @POST("createUser.php")
    fun createUser(@Body body: CreateUserBody): Observable<User>

    @POST("login.php")
    fun login(@Body body: LoginBody): Observable<User>

    @POST("getBares.php")
    fun getBars(@Body body: BarBody): Observable<ArrayList<BarModel>?>

    @POST("addBar.php")
    fun addBar(@Body body: AddBarBody): Observable<AddBarResponse>

    @GET("getUser.php")
    fun getUser(@Query("token") token: String): Observable<User>
}