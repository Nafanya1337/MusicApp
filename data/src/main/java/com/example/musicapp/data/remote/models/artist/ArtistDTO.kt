package com.example.musicapp.data.remote.models.artist


import com.google.gson.annotations.SerializedName
import java.net.URL

data class ArtistDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("share")
    val share: URL?,
    @SerializedName("picture_big")
    val picture: String?,
    @SerializedName("tracklist")
    val tracklist: String
)
