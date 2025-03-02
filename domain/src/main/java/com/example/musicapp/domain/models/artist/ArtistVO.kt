package com.example.musicapp.domain.models.artist

import java.net.URL

data class ArtistVO(
    val id: Long = 0,
    val name: String = "",
    val share: URL? = null,
    val picture: String = "",
    val fans: Int = 0
)
