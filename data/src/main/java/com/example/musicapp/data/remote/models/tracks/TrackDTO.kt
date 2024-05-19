package com.example.musicapp.data.remote.models.tracks

import com.example.musicapp.data.remote.models.artist.ArtistDTO
import com.google.gson.annotations.SerializedName

data class TrackDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("artist")
    val artist: ArtistDTO,
    @SerializedName("album")
    val album: AlbumInfoDTO?,
    @SerializedName("track_position")
    val position: Int?
)

