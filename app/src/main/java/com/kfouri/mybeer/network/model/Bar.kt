package com.kfouri.mybeer.network.model

import com.google.gson.annotations.SerializedName

data class BarModel (
    @SerializedName("id") val id : Int,
    @SerializedName("nombre") val nombre : String,
    @SerializedName("direccion") val direccion : String,
    @SerializedName("lat") val lat : Double,
    @SerializedName("lon") val lon : Double,
    @SerializedName("logo") val logo : String,
    @SerializedName("distance") val distance : Double,
    @SerializedName("servicios") val servicios: ArrayList<ServicioModel>?)

data class ServicioModel(@SerializedName("id") var id: String,
                         @SerializedName("descripcion") var descripcion: String,
                         @SerializedName("icon") var icon: String)

data class BarBody(@SerializedName("lat") var lat: String,
                   @SerializedName("lon") var lon: String,
                   @SerializedName("radius") var radius: String)