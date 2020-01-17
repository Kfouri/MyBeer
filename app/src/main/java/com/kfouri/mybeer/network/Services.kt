package com.kfouri.mybeer.network

import com.kfouri.mybeer.network.model.BarModel
import com.kfouri.mybeer.network.model.BarBody
import com.kfouri.mybeer.network.model.LoginBody
import com.kfouri.mybeer.network.model.User
import retrofit2.http.Body
import retrofit2.http.POST
import rx.Observable

interface APIService {

    @POST("login.php")
    fun login(@Body body: LoginBody): Observable<User>

    @POST("getBares.php")
    fun getBars(@Body body: BarBody): Observable<ArrayList<BarModel>>
}