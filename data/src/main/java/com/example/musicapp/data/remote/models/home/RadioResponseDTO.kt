package com.example.musicapp.data.remote.models.home

import android.os.Parcelable
import com.example.musicapp.data.remote.models.ContributorsDTO
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RadioResponseDTO(
    @SerializedName("data")
    val data: List<RadioDTO>
) : Parcelable

@Parcelize
data class RadioDTO(
    @SerializedName("id")
    val id: Long,
    @SerializedName("title")
    val title: String,
    @SerializedName("picture_medium")
    val picture: String,
    @SerializedName("tracklist")
    val trackList: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("artist")
    val constributor: List<ContributorsDTO>?
) : Parcelable