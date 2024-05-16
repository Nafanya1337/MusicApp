package com.example.musicapp.data.remote.models.artist

import com.example.musicapp.data.remote.models.TrackDTO
import com.example.musicapp.domain.models.ContributorsVO
import com.google.gson.annotations.SerializedName
import java.util.Date

data class AlbumResponse(
    @SerializedName("data")
    val data: List<AlbumDTO>
)

data class AlbumDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover_big")
    val cover: String,
    @SerializedName("genre_id")
    val genre: Int,
    @SerializedName("release_date")
    val releaseDate: Date,
    @SerializedName("record_type")
    val recordType: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("tracklist")
    val tracklist: String,
    var contributors: List<ContributorsVO> = emptyList()
    )
