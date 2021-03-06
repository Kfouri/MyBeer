package com.kfouri.mybeer.network.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class BarModel (
    @SerializedName("id") val id: Int,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("ciudad") val ciudad: String?,
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double,
    @SerializedName("logo") val logo: String,
    @SerializedName("distance") val distance: Double,
    @SerializedName("rating") val rating: Double,
    @SerializedName("votes") val votes: Long,
    @SerializedName("myVote") val myVote: Float,
    @SerializedName("type") val type: Int = 1,
    @SerializedName("servicios") val servicios: ArrayList<ServicioModel>?) : Serializable

data class ServicioModel(@SerializedName("id") var id: String,
                         @SerializedName("descripcion") var descripcion: String,
                         @SerializedName("icon") var icon: String) : Serializable

data class BarBody(@SerializedName("lat") var lat: String,
                   @SerializedName("lon") var lon: String,
                   @SerializedName("radius") var radius: String)

data class AddBarBody(@SerializedName("nombre") val nombre: String,
                      @SerializedName("direccion") val direccion: String,
                      @SerializedName("ciudad") val ciudad: String,
                      @SerializedName("pais") val pais: String,
                      @SerializedName("lat") val lat: String,
                      @SerializedName("lon") val lon: String,
                      @SerializedName("logo") val logo: String? = null)

data class AddBarResponse(@SerializedName("code") val code: String)

data class RatingBarBody(@SerializedName("id_bar") val id_bar: Int,
                         @SerializedName("id_user") val id_user: Int,
                         @SerializedName("value") val value: Float)

data class RatingBarResponse(@SerializedName("code") val code: String)

data class BarRequest(@SerializedName("id_bar") var idBar: Int,
                      @SerializedName("id_user") var idUser: Int,
                      @SerializedName("lat") var lat: Double,
                      @SerializedName("lon") var lon: Double)
