package com.kfouri.mybeer.network.model

import com.google.gson.annotations.SerializedName

data class User (
    @SerializedName("id") val id : Int,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password : String,
    @SerializedName("nombre") val nombre : String,
    @SerializedName("token") val token: String)

data class CreateUserBody (
    @SerializedName("email") val email: String,
    @SerializedName("password") val password : String,
    @SerializedName("nombre") val nombre : String)