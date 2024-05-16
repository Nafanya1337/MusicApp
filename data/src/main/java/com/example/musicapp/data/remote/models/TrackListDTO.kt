package com.example.musicapp.data.remote.models

import com.google.gson.annotations.SerializedName


data class TrackListDTO(
    @SerializedName("data")
    val list: List<TrackDTO>,
    val title: String)
