package com.example.musicapp.domain.models.home

import com.example.musicapp.domain.models.ContributorsVO

data class RadioResponseVO (
    val data: List<RadioVO>
)


data class RadioVO(
    val id: Long,
    val title: String,
    val picture: String,
    val trackList: String,
    val type: String,
    val contributor: List<ContributorsVO>?
)
