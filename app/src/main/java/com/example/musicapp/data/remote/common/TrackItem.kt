package com.example.musicapp.data.remote.common

import com.google.gson.annotations.SerializedName

data class TrackItem(
    val name: String,
    val artist: String,
    val url: String,
    val streamable: String,
    val listeners: String,
    val image: List<Image>
)

data class Image(
    @SerializedName("#text")
    val text: String,
    val size: String
)