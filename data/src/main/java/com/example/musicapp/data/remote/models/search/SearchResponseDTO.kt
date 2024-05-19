package com.example.musicapp.data.remote.models.search

import com.example.musicapp.data.remote.models.tracks.TrackDTO
import com.google.gson.annotations.SerializedName

data class SearchResponseDTO(
    @SerializedName("data")
    val list: List<TrackDTO>,
    val status: StatusClassDTO
)

data class ChartResponseDTO(
    @SerializedName("tracks")
    val tracks: TrackResponseDTO
)

data class TrackResponseDTO(
    @SerializedName("data")
    val data: List<TrackDTO>
)