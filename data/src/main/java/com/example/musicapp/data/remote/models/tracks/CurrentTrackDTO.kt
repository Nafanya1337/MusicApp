package com.example.musicapp.data.remote.models.tracks

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlin.time.Duration

data class CurrentTrackDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("link")
    val link: String,
    @SerializedName("share")
    val share: String,
    @SerializedName("duration")
    val duration: Int,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("explicit_lyrics")
    val explicitLyrics: Boolean,
    @SerializedName("preview")
    val preview: String,
    @SerializedName("contributors")
    val contributors: List<ContributorsDTO>,
    @SerializedName("album")
    val album: AlbumInfoDTO
)

data class AlbumInfoDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("cover_big")
    val picture: String
)

@Parcelize
data class ContributorsDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("name")
    val name: String,
    @SerializedName("picture_small")
    val picture: String
) : Parcelable
