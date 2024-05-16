package com.example.musicapp.domain.models.artist

import java.net.URL

data class ArtistVO(
    val id: Long,
    val name: String,
    val share: URL?,
    val picture: String?,
    val tracklist: String
)
