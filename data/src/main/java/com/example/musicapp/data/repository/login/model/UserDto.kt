package com.example.musicapp.data.repository.login.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserDto(
    val id: String,
    val email: String,
    val nickname: String,
    val photo: String
) : Parcelable
